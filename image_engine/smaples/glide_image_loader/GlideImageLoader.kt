package com.glide_image_loader

import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import androidx.exifinterface.media.ExifInterface.*
import com.bumptech.glide.*
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.data.HttpUrlFetcher
import com.bumptech.glide.load.data.StreamAssetPathFetcher
import com.bumptech.glide.load.data.StreamLocalUriFetcher
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader
import com.bumptech.glide.load.resource.SimpleResource
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.signature.ObjectKey
import com.colovaria.image_engine.api.resources.*
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import android.content.Context
import android.graphics.Bitmap
import androidx.collection.LruCache
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.FutureTarget
import com.colovaria.geometry.Size
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

class GlideImageLoader(private val context: Context) : ImageLoader {
    private val executor = Executors.newSingleThreadExecutor()
    private val sizeCache = LruCache<ResourcePath, Size>(MAX_SIZE_CACHE_SIZE)

    override fun load(resource: ResourcePath, targetSize: Size?): CompletableFuture<Bitmap> {
        return CompletableFuture.supplyAsync({
            Glide.with(context).asBitmap()
                .load(resource)
                .submit(targetSize)
                .get()
        }, executor)
    }

    override fun size(resource: ResourcePath): CompletableFuture<Size> {
        sizeCache[resource]?.also { return CompletableFuture.completedFuture(it) }

        return CompletableFuture.supplyAsync({
            Glide.with(context).asSize()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .load(resource)
                .submit()
                .get()
        }, executor).thenApply {
            sizeCache.put(resource, it)
        }
    }

    private fun <T> RequestBuilder<T>.submit(size: Size?) : FutureTarget<T> {
        return when (size) {
            null -> submit()
            else -> submit(size.width, size.height)
        }
    }

    private fun RequestManager.asSize() : RequestBuilder<Size> {
        return `as`(Size::class.java)
    }

    companion object {
        private const val MAX_SIZE_CACHE_SIZE = 10000
    }
}

class BitmapSizeDecoder : ResourceDecoder<InputStream, BitmapFactory.Options> {
    override fun handles(inputStream: InputStream, options: Options) : Boolean {
        return true
    }

    override fun decode(inputStream: InputStream, width: Int, height: Int, options: Options) : Resource<BitmapFactory.Options> {
        return SimpleResource(BitmapFactory.Options().apply {
            inJustDecodeBounds = true

            assert(inputStream.markSupported())
            inputStream.mark(0)

            BitmapFactory.decodeStream(inputStream, null, this)?.recycle()

            inputStream.reset()

            // Since we skip the glide code that transpose images, we need to do it manually.
            if (shouldTranspose(inputStream)) {
                val transposedWidth = outHeight
                val transposedHeight = outWidth
                outWidth = transposedWidth
                outHeight = transposedHeight
            }
        })
    }

    private fun shouldTranspose(inputStream: InputStream) : Boolean {
        return when (ExifInterface(inputStream).getAttributeInt(TAG_ORIENTATION, ORIENTATION_NORMAL)) {
            ORIENTATION_ROTATE_90, ORIENTATION_ROTATE_270 -> true
            else -> false
        }
    }
}

@GlideModule
class GlideWithImageLoader : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.prepend(ResourcePath::class.java, InputStream::class.java,
            ResourcePathLoaderFactory(context)
        )
        registry.prepend(InputStream::class.java, BitmapFactory.Options::class.java,
            BitmapSizeDecoder()
        )
        registry.register(BitmapFactory.Options::class.java, Size::class.java,
            OptionsSizeResourceTranscoder()
        )
    }
}

class OptionsSizeResourceTranscoder : ResourceTranscoder<BitmapFactory.Options, Size> {
    override fun transcode(resource: Resource<BitmapFactory.Options>, options: Options) : Resource<Size> {
        return SimpleResource(resource.get().run { Size(outWidth, outHeight) })
    }
}

class ResourcePathLoader(
    private val context: Context,
) : ModelLoader<ResourcePath, InputStream> {
    override fun buildLoadData(model: ResourcePath, width: Int, height: Int, options: Options) : ModelLoader.LoadData<InputStream> {
        return ModelLoader.LoadData(
            ObjectKey(model), when (model) {
                is AssetResourcePath -> StreamAssetPathFetcher(context.assets, model.path)
                is InternalStorageResourcePath -> StreamFilePath(model.path)
                is ExternalStorageResourcePath -> StreamLocalUriFetcher(context.contentResolver, model.uri)
                is WebResourcePath -> HttpUrlFetcher(GlideUrl(model.url), options.get(HttpGlideUrlLoader.TIMEOUT) ?: 0)
                is AndroidResResourcePath -> error("Can't load from android resources")
            })
    }

    override fun handles(model: ResourcePath) : Boolean {
        return true
    }

    class StreamFilePath(private val path: String) : DataFetcher<InputStream> {
        private var inputStream: InputStream? = null

        override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
            try {
                inputStream?.close()
                inputStream = FileInputStream(path)
                callback.onDataReady(inputStream)
            } catch (e: FileNotFoundException) {
                callback.onLoadFailed(e)
            }
        }

        override fun cleanup() {
            inputStream?.close()
        }

        override fun cancel() {}

        override fun getDataClass() = InputStream::class.java

        override fun getDataSource() = DataSource.LOCAL
    }
}

class ResourcePathLoaderFactory(
    private val context: Context,
) : ModelLoaderFactory<ResourcePath, InputStream> {
    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<ResourcePath, InputStream> {
        return  ResourcePathLoader(context)
    }

    override fun teardown() {}
}
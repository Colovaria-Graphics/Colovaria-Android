package com.glide_image_loader

import android.content.Context
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.data.HttpUrlFetcher
import com.bumptech.glide.load.data.StreamAssetPathFetcher
import com.bumptech.glide.load.data.StreamLocalUriFetcher
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader
import com.bumptech.glide.signature.ObjectKey
import com.colovaria.image_engine.api.resources.*
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream

class ResourcePathLoader(
    private val context: Context,
) : ModelLoader<ResourcePath, InputStream> {
    override fun buildLoadData(model: ResourcePath, width: Int, height: Int, options: Options) : ModelLoader.LoadData<InputStream> {
        return ModelLoader.LoadData(ObjectKey(model), when (model) {
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
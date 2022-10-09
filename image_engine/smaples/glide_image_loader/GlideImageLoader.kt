package com.glide_image_loader

import android.content.Context
import android.graphics.Bitmap
import androidx.collection.LruCache
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.FutureTarget
import com.colovaria.geometry.Size
import com.colovaria.image_engine.api.resources.ImageLoader
import com.colovaria.image_engine.api.resources.ResourcePath
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
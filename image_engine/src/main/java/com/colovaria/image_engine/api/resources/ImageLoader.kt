package com.colovaria.image_engine.api.resources

import android.graphics.Bitmap
import com.colovaria.geometry.Size
import java.util.concurrent.CompletableFuture

interface ImageLoader {
    fun load(resource: ResourcePath, targetSize: Size? = null): CompletableFuture<Bitmap>

    fun size(resource: ResourcePath) : CompletableFuture<Size>
}
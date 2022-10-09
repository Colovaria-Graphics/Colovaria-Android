package com.colovaria.image_engine.export

import android.content.Context
import android.graphics.Bitmap
import com.colovaria.geometry.Size
import com.colovaria.image_engine.api.Frame
import com.colovaria.image_engine.api.resources.ImageLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

class DynamicMultiExporter(
    private val context: Context,
    count: Int = 4,
    private val imageLoaderProvider: () -> ImageLoader,
    private val optimizeImageSize: Boolean = false
) {
    private val executors = Executors.newFixedThreadPool(count)

    fun export(frame: Frame, size: Size) : CompletableFuture<Bitmap> = CompletableFuture.supplyAsync({
        val exporter = Exporter(context, size, imageLoaderProvider, optimizeImageSize)
        try {
            return@supplyAsync exporter.export(frame)
        } finally {
            exporter.dispose()
        }
    }, executors).thenCompose { it }

    fun dispose() {
        executors.submit {
            executors.shutdown()
        }
    }
}
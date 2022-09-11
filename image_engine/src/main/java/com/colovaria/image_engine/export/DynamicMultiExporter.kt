package com.colovaria.image_engine.export

import android.content.Context
import android.graphics.Bitmap
import com.colovaria.geometry.Size
import com.colovaria.image_engine.api.Frame
import com.colovaria.image_engine.api.resources.ImageLoader
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executors

// TODO: optimize it.
class DynamicMultiExporter(
    private val context: Context,
    count: Int = 4,
    private val imageLoaderProvider: () -> ImageLoader,
    private val optimizeImageSize: Boolean = false
) {
    private val executors = Executors.newFixedThreadPool(count)
    private val scheduler = Schedulers.from(executors)

    fun export(frame: Frame, size: Size) = Single.create<Bitmap> {
        val exporter = Exporter(context, size, imageLoaderProvider, optimizeImageSize)
        try {
            it.onSuccess(exporter.export(frame).blockingGet())
        } catch (e: Exception) {
            it.onError(e)
        } finally {
            exporter.dispose()
        }
    }.subscribeOn(scheduler)

    fun dispose() {
        scheduler.scheduleDirect {
            executors.shutdown()
            scheduler.shutdown()
        }
    }
}
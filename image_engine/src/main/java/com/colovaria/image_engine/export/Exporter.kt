package com.colovaria.image_engine.export

import android.content.Context
import android.graphics.Bitmap
import com.colovaria.geometry.Size
import com.colovaria.graphics.BindReference
import com.colovaria.graphics.GFrameBuffer
import com.colovaria.graphics.egl.GContext
import com.colovaria.graphics.egl.GDisplay
import com.colovaria.graphics.egl.GSurface
import com.colovaria.image_engine.FrameCompositor
import com.colovaria.image_engine.api.Frame
import com.colovaria.image_engine.api.resources.ImageLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

class Exporter(
    private val context: Context,
    private val size: Size,
    imageLoaderProvider: () -> ImageLoader,
    private val optimizeImageSize: Boolean = false
) {
    private val executor = Executors.newSingleThreadExecutor()

    private lateinit var frameCompositor : FrameCompositor
    private lateinit var display : GDisplay
    private lateinit var gpuContext : GContext
    private lateinit var surface : GSurface
    private lateinit var frameBuffer: GFrameBuffer
    private lateinit var contextBindReference: BindReference

    init {
        executor.submit {
            display = GDisplay.Factory.create()
            gpuContext = GContext.Factory.create(display)
            surface = GSurface.Factory.create(display, size)

            contextBindReference = gpuContext.bind(surface)

            frameCompositor = FrameCompositor(context, size, imageLoaderProvider, optimizeImageSize)
            frameBuffer = GFrameBuffer(size)
        }
    }

    fun export(frame: Frame) : CompletableFuture<Bitmap> = CompletableFuture.supplyAsync({
        frameBuffer.withBind {
            frameCompositor.render(frame, false)
        }
        gpuContext.swapBuffers(surface)

        return@supplyAsync frameBuffer.save()
    }, executor)

    fun size() = size

    fun dispose() {
        executor.submit {
            frameBuffer.dispose()
            frameCompositor.dispose()
            surface.dispose()
            display.dispose()
            contextBindReference.unbind()
            gpuContext.dispose()
            executor.shutdown()
        }
    }
}
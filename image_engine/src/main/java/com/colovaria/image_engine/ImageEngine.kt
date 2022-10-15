package com.colovaria.image_engine

import android.content.Context
import android.graphics.SurfaceTexture
import android.os.Handler
import android.os.HandlerThread
import android.os.SystemClock
import android.view.Surface
import com.colovaria.geometry.Size
import com.colovaria.graphics.BindReference
import com.colovaria.graphics.egl.GContext
import com.colovaria.graphics.egl.GDisplay
import com.colovaria.graphics.egl.GSurface
import com.colovaria.image_engine.api.Frame
import com.colovaria.image_engine.api.resources.ImageLoader
import java.lang.Long.max
import java.util.*
import java.util.concurrent.TimeUnit

class ImageEngine(
    private val context: Context,
    private val imageLoaderProvider: () -> ImageLoader,
    fps: Int = 60
) {
    private val msBetweenFrames = TimeUnit.SECONDS.toMillis(1) / fps

    private val handlerThread = HandlerThread(UUID.randomUUID().toString()).apply { start() }
    private val handler = Handler(handlerThread.looper)

    private lateinit var display: GDisplay
    private lateinit var gpuContext: GContext

    private var frameCompositor: FrameCompositor? = null

    private var contextBindReference: BindReference? = null
    private var surface: GSurface? = null

    private var frame = Frame()
    private var lastFrame: Frame? = null
    private var forceRenderNextFrame = false

    @Volatile
    private var isRunning: Boolean = true

    init {
        handler.post { initInternal() }
    }

    fun setTargetSurface(newSurface: Any?) {
        handler.post { setTargetSurfaceInternal(newSurface) }
    }

    fun setFrame(newFrame: Frame) {
        handler.post { setFrameInternal(newFrame) }
    }

    fun isRunning() = isRunningInternal()

    fun start() {
        handler.post { startInternal() }
    }

    fun stop() {
        handler.post { stopInternal() }
    }

    fun dispose() {
        handler.post { disposeInternal() }
    }

    private fun initInternal() {
        display = GDisplay.create()
        gpuContext = GContext.create(display)
        setTargetSurfaceInternal(null)
    }

    private fun setTargetSurfaceInternal(newSurface: Any?) {
        val wasRunning = isRunningInternal()
        stopInternal()

        contextBindReference?.unbind()
        surface?.dispose()

        surface = when (newSurface) {
            null -> GSurface.create(display, Size(1, 1))
            is Surface -> GSurface.create(display, newSurface)
            is SurfaceTexture -> GSurface.create(display, newSurface)
            else -> error("Unknown surface type: $newSurface")
        }

        contextBindReference = gpuContext.bind(surface!!)
        forceRenderNextFrame = true

        frameCompositor?.dispose()
        frameCompositor = FrameCompositor(context, surface!!.size, imageLoaderProvider)

        if (wasRunning) startInternal()
    }

    private fun setFrameInternal(newFrame: Frame) {
        frame = newFrame
    }

    private fun isRunningInternal() = isRunning

    private fun startInternal() {
        if (isRunningInternal()) return
        isRunning = true
        scheduleNextRender()
    }

    private fun scheduleNextRender() {
        if (!isRunning) return

        val currentTime = SystemClock.uptimeMillis()
        renderInternal()
        val frameDuration = max(SystemClock.uptimeMillis() - currentTime, 0)
        handler.postDelayed({ scheduleNextRender() }, max(msBetweenFrames - frameDuration, 0))
    }

    private fun stopInternal() {
        if (!isRunningInternal()) return
        isRunning = false
    }

    private fun disposeInternal() {
        stopInternal()
        frameCompositor?.dispose()
        contextBindReference?.unbind()
        surface?.dispose()
        display.dispose()
        gpuContext.dispose()
        handlerThread.quit()
    }

    private fun renderInternal() {
        if (frame == lastFrame && !forceRenderNextFrame) return
        forceRenderNextFrame = false

        frameCompositor?.render(frame)
        gpuContext.swapBuffers(surface!!)
        lastFrame = frame
    }
}
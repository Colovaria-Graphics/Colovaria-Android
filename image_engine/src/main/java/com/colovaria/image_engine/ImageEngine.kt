package com.colovaria.image_engine

import android.content.Context
import android.graphics.SurfaceTexture
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import com.colovaria.graphics.egl.GContext
import com.colovaria.graphics.egl.GDisplay
import com.colovaria.graphics.egl.GSurface
import com.colovaria.geometry.Size
import com.colovaria.graphics.BindReference
import com.colovaria.image_engine.api.Frame
import com.colovaria.image_engine.api.resources.ImageLoader
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ImageEngine(
    private val context: Context,
    private val imageLoaderProvider: () -> ImageLoader,
    fps: Int = 60
) {
    private val msBetweenFrames = TimeUnit.SECONDS.toMillis(1) / fps

    private val handlerThread = HandlerThread("31bfa0078b171885fab0232cfabec7d7").apply { start() }
    private val handler = Handler(handlerThread.looper)
    private val scheduler = Schedulers.from(handler::post)

    private lateinit var display: GDisplay
    private lateinit var gpuContext: GContext

    private var frameCompositor: FrameCompositor? = null

    private var contextBindReference: BindReference? = null
    private var surface: GSurface? = null

    private var frame = Frame()
    private var lastFrame: Frame? = null
    private var forceRenderNextFrame = false

    @Volatile
    private var renderTask: Disposable? = null

    init {
        scheduler.scheduleDirect { initInternal() }
    }

    fun setTargetSurface(newSurface: Any?) {
        scheduler.scheduleDirect { setTargetSurfaceInternal(newSurface) }
    }

    fun setFrame(newFrame: Frame) {
        scheduler.scheduleDirect { setFrameInternal(newFrame) }
    }

    fun isRunning() = isRunningInternal()

    fun start() {
        scheduler.scheduleDirect { startInternal() }
    }

    fun stop() {
        scheduler.scheduleDirect { stopInternal() }
    }

    fun dispose() {
        scheduler.scheduleDirect { disposeInternal() }
    }

    private fun initInternal() {
        display = GDisplay.Factory.create()
        gpuContext = GContext.Factory.create(display)
        setTargetSurfaceInternal(null)
    }

    private fun setTargetSurfaceInternal(newSurface: Any?) {
        val wasRunning = isRunningInternal()
        stopInternal()

        contextBindReference?.unbind()
        surface?.dispose()

        surface = when (newSurface) {
            null -> GSurface.Factory.create(display, Size(1, 1))
            is Surface -> GSurface.Factory.create(display, newSurface)
            is SurfaceTexture -> GSurface.Factory.create(display, newSurface)
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

    private fun isRunningInternal() = renderTask != null

    private fun startInternal() {
        if (isRunningInternal()) return
        // TODO: find a better way to schedule rendering, this function ignores the times it takes to execute one render pass.
        renderTask = scheduler.schedulePeriodicallyDirect({ renderInternal() }, 0, msBetweenFrames, TimeUnit.MILLISECONDS)
    }

    private fun stopInternal() {
        if (!isRunningInternal()) return
        renderTask?.dispose()
        renderTask = null
    }

    private fun disposeInternal() {
        stopInternal()
        frameCompositor?.dispose()
        contextBindReference?.unbind()
        surface?.dispose()
        display.dispose()
        gpuContext.dispose()
        scheduler.shutdown()
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
package com.colovaria.graphics.egl

import android.graphics.SurfaceTexture
import android.opengl.EGL14
import android.opengl.EGLSurface
import android.view.Surface
import com.colovaria.geometry.Size
import com.colovaria.graphics.base.GObject

class GSurface private constructor(
    val display: GDisplay,
    val eglSurface: EGLSurface,
) : GObject() {
    val width: Int
    get() {
        val width = intArrayOf(0)
        assert(EGL.eglQuerySurface(display.eglDisplay, eglSurface, EGL14.EGL_WIDTH, width, 0))
        return width[0]
    }

    val height: Int
    get() {
        val height = intArrayOf(0)
        assert(EGL.eglQuerySurface(display.eglDisplay, eglSurface, EGL14.EGL_HEIGHT, height, 0))
        return height[0]
    }

    val size: Size
    get() = Size(width, height)

    override fun dispose() {
        assert(EGL.eglDestroySurface(display.eglDisplay, eglSurface))
        disposed = true
    }

    companion object {
        fun create(display: GDisplay, size: Size) : GSurface {
            return GSurface(display,
                EGL.eglCreatePbufferSurface(display.eglDisplay,
                    display.eglConfig,
                    intArrayOf(EGL14.EGL_WIDTH, size.width, EGL14.EGL_HEIGHT, size.height, EGL14.EGL_NONE),
                    0))
        }

        fun create(display: GDisplay, surface: Surface): GSurface {
            val eglSurface = EGL.eglCreateWindowSurface(
                display.eglDisplay,
                display.eglConfig,
                surface,
                intArrayOf(EGL14.EGL_NONE),
                0
            )
            assert(eglSurface.nativeHandle != EGL14.EGL_BAD_SURFACE.toLong())
            return GSurface(display, eglSurface)
        }

        fun create(display: GDisplay, surface: SurfaceTexture): GSurface {
            val eglSurface = EGL.eglCreateWindowSurface(display.eglDisplay,
                display.eglConfig,
                surface,
                intArrayOf(EGL14.EGL_NONE),
                0)
            assert(eglSurface.nativeHandle != EGL14.EGL_BAD_SURFACE.toLong())
            return GSurface(display, eglSurface)
        }
    }
}

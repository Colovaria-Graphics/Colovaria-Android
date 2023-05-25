package com.colovaria.graphics.egl

import android.opengl.EGL14
import android.opengl.EGLContext
import com.colovaria.graphics.base.BindReference
import com.colovaria.graphics.base.GObject

class GContext private constructor(
    val eglContext: EGLContext,
    val display: GDisplay,
) : GObject() {
    fun bind(surface: GSurface) : BindReference {
        assert(EGL.eglMakeCurrent(display.eglDisplay, surface.eglSurface, surface.eglSurface, eglContext))

        return BindReference {
            assert(EGL.eglMakeCurrent(display.eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT))
        }
    }

    inline fun <T> withBind(surface: GSurface, func: () -> T) : T {
        val bindReference = bind(surface)
        try {
            return func()
        } finally {
            bindReference.unbind()
        }
    }

    fun swapBuffers(surface: GSurface) {
        try {
            EGL.eglSwapBuffers(display.eglDisplay, surface.eglSurface)
        } catch (e: EGLException) {
            if (e.glCode == EGL14.EGL_BAD_SURFACE) {
                // This is OK, do nothing
            } else {
                throw e
            }
        }
    }

    override fun actualDispose() {
        assert(EGL.eglDestroyContext(display.eglDisplay, eglContext))
    }

    companion object {
        fun create(display: GDisplay) : GContext {
            return GContext(EGL.eglCreateContext(display.eglDisplay, display.eglConfig,
                EGL14.EGL_NO_CONTEXT, intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 3, EGL14.EGL_NONE), 0), display)
        }
    }
}
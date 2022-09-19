package com.colovaria.graphics.egl

import android.opengl.EGL14
import android.opengl.EGLContext
import com.colovaria.graphics.BindReference
import com.colovaria.graphics.GObject

class GContext private constructor(
    val eglContext: EGLContext,
    val display: GDisplay,
) : GObject() {
    fun bind(surface: GSurface) : BindReference {
        assert(EGL14.eglMakeCurrent(display.eglDisplay, surface.eglSurface, surface.eglSurface, eglContext))

        return BindReference {
            assert(EGL14.eglMakeCurrent(display.eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT))
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
        if (!EGL14.eglSwapBuffers(display.eglDisplay, surface.eglSurface)) {
            when (EGL14.eglGetError()) {
                EGL14.EGL_SUCCESS -> return
                EGL14.EGL_CONTEXT_LOST -> error("handle context lose")
                EGL14.EGL_BAD_SURFACE -> return // TODO: can we handle it better?
                else -> error(this)
            }
        }
    }

    override fun dispose() {
        assert(EGL14.eglDestroyContext(display.eglDisplay, eglContext))
        disposed = true
    }

    companion object {
        fun create(display: GDisplay) : GContext {
            return GContext(EGL14.eglCreateContext(display.eglDisplay, display.eglConfig,
                EGL14.EGL_NO_CONTEXT, intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 3, EGL14.EGL_NONE), 0), display)
        }
    }
}
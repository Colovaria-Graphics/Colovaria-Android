package com.colovaria.graphics.egl

import android.opengl.*

object EGL {
    fun eglMakeCurrent(
        dpy: EGLDisplay,
        draw: EGLSurface,
        read: EGLSurface,
        ctx: EGLContext
    ) = wrapWithErrorCheck {
        return@wrapWithErrorCheck EGL14.eglMakeCurrent(dpy, draw, read, ctx)
    }

    fun eglTerminate(dpy: EGLDisplay) = wrapWithErrorCheck {
        return@wrapWithErrorCheck EGL14.eglTerminate(dpy)
    }

    fun eglChooseConfig(
        dpy: EGLDisplay,
        attrib_list: IntArray,
        attrib_listOffset: Int,
        configs: Array<EGLConfig?>?,
        configsOffset: Int,
        config_size: Int,
        num_config: IntArray,
        num_configOffset: Int
    ) = wrapWithErrorCheck {
        return@wrapWithErrorCheck EGL14.eglChooseConfig(dpy, attrib_list, attrib_listOffset,
            configs, configsOffset, config_size, num_config, num_configOffset)
    }

    fun eglGetDisplay(display_id: Int) = wrapWithErrorCheck {
        return@wrapWithErrorCheck EGL14.eglGetDisplay(display_id)
    }

    fun eglInitialize(
        dpy: EGLDisplay ,
        major: IntArray,
        majorOffset: Int,
        minor: IntArray,
        minorOffset: Int
    ) = wrapWithErrorCheck {
        return@wrapWithErrorCheck EGL14.eglInitialize(dpy, major, majorOffset, minor, minorOffset)
    }

    fun eglSwapBuffers(dpy: EGLDisplay, surface: EGLSurface) = wrapWithErrorCheck {
        return@wrapWithErrorCheck EGL14.eglSwapBuffers(dpy, surface)
    }

    fun eglDestroyContext(dpy: EGLDisplay, ctx: EGLContext) = wrapWithErrorCheck {
        return@wrapWithErrorCheck EGL14.eglDestroyContext(dpy, ctx)
    }

    fun eglCreateContext(
        dpy: EGLDisplay,
        config: EGLConfig,
        share_context: EGLContext,
        attrib_list: IntArray,
        offset: Int
    ) = wrapWithErrorCheck {
        return@wrapWithErrorCheck EGL14.eglCreateContext(dpy, config, share_context, attrib_list, offset)
    }

    fun eglQuerySurface(
        dpy: EGLDisplay,
        surface: EGLSurface,
        attribute: Int,
        value: IntArray,
        offset: Int
    ) = wrapWithErrorCheck {
        return@wrapWithErrorCheck EGL14.eglQuerySurface(dpy, surface, attribute, value, offset)
    }

    fun eglDestroySurface(dpy: EGLDisplay, surface: EGLSurface) = wrapWithErrorCheck {
        return@wrapWithErrorCheck EGL14.eglDestroySurface(dpy, surface)
    }

    fun eglCreatePbufferSurface(
        dpy: EGLDisplay,
        config: EGLConfig,
        attrib_list: IntArray,
        offset: Int
    ) = wrapWithErrorCheck {
        return@wrapWithErrorCheck EGL14.eglCreatePbufferSurface(dpy, config, attrib_list, offset)
    }

    fun eglCreateWindowSurface(
        dpy: EGLDisplay,
        config: EGLConfig,
        win: Any,
        attrib_list: IntArray,
        offset: Int
    ) = wrapWithErrorCheck {
        return@wrapWithErrorCheck EGL14.eglCreateWindowSurface(dpy, config, win, attrib_list, offset)
    }

    fun eglGetConfigAttrib(
        dpy: EGLDisplay,
        config: EGLConfig,
        attribute: Int,
        value: IntArray,
        offset: Int
    ) = wrapWithErrorCheck {
        return@wrapWithErrorCheck EGL14.eglGetConfigAttrib(dpy, config, attribute, value, offset)
    }

    private inline fun <R> wrapWithErrorCheck(func: () -> R) : R {
        val result = func()
        val errorCode = EGL14.eglGetError()
        if (errorCode != EGL14.EGL_SUCCESS) {
            throw EGLException(errorCode)
        }
        return result
    }
}

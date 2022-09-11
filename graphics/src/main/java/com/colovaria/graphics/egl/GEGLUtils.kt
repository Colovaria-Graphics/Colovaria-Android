package com.colovaria.graphics.egl

import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLDisplay

object GEGLUtils {
    fun findConfigAttrib(
        display: EGLDisplay,
        config: EGLConfig,
        attribute: Int,
        defaultValue: Int = -1,
        tempArray: IntArray = IntArray(1)) : Int {
        return if (EGL14.eglGetConfigAttrib(display, config, attribute, tempArray, 0)) tempArray[0] else defaultValue
    }
}
package com.colovaria.graphics.egl

import android.opengl.EGLConfig
import android.opengl.EGLDisplay

object GUtils {
    fun findConfigAttrib(
        display: EGLDisplay,
        config: EGLConfig,
        attribute: Int,
        defaultValue: Int = -1,
        tempArray: IntArray = IntArray(1)) : Int {
        return if (EGL.eglGetConfigAttrib(display, config, attribute, tempArray, 0)) tempArray[0] else defaultValue
    }
}
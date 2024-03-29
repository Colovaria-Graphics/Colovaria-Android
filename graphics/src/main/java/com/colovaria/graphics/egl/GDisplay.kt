package com.colovaria.graphics.egl

import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLDisplay
import com.colovaria.graphics.base.GObject

class GDisplay private constructor(
    val eglDisplay: EGLDisplay,
    val eglConfig: EGLConfig
) : GObject() {
    override fun actualDispose() {
        assert(EGL.eglTerminate(eglDisplay))
    }

    object Utils {
        fun chooseConfig(eglDisplay: EGLDisplay, displaySpec: GDisplayConfigSpec) : EGLConfig {
            val configSpecArray = displaySpec.toIntArray()
            val configsNumArray = intArrayOf(0)
            assert(EGL.eglChooseConfig(eglDisplay, configSpecArray, 0, null, 0, 0, configsNumArray, 0))

            val configsNum = configsNumArray[0]
            assert(configsNum >= 0)

            val configs = Array<EGLConfig?>(configsNum) { null }
            assert(EGL.eglChooseConfig(eglDisplay, configSpecArray, 0, configs, 0, configsNum, configsNumArray, 0))

            return configs.filterNotNull()
                .map { it to displaySpec.computeMatchDistance(eglDisplay, it) }
                .filter { it.second != null }
                .minByOrNull { it.second!! }?.first ?: error("Can't find matching config")
        }
    }

    companion object {
        fun create(displaySpec: GDisplayConfigSpec = GDisplayConfigSpec()) : GDisplay {
            val eglDisplay = EGL.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)

            assert(eglDisplay != EGL14.EGL_NO_DISPLAY)
            assert(EGL.eglInitialize(eglDisplay, intArrayOf(0), 0, intArrayOf(0), 0))

            return GDisplay(eglDisplay, Utils.chooseConfig(eglDisplay, displaySpec))
        }
    }
}
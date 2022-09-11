package com.graphics

import android.opengl.GLES32

abstract class GHandle(val handle: Int) : GObject() {
    init {
        assert(handle != GLES32.GL_INVALID_VALUE)
    }
}
package com.colovaria.graphics

import android.opengl.GLES20

abstract class GHandle(val handle: Int) : GObject() {
    init {
        assert(handle != GLES20.GL_INVALID_VALUE)
    }
}
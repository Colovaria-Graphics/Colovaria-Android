package com.colovaria.graphics.gles

import android.opengl.GLES20
import com.colovaria.graphics.base.GObject

abstract class GHandle(val handle: Int) : GObject() {
    init {
        assert(handle != GLES20.GL_INVALID_VALUE)
    }
}

package com.image_engine.api.mask

import android.opengl.GLES32
import com.graphics.GUniform

enum class MaskType(private val value: Int) : GUniform {
    ALPHA(0);

    override fun putUniform(attribLocation: Int) {
        GLES32.glUniform1i(attribLocation, value)
    }
}
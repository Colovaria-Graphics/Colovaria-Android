package com.colovaria.image_engine.api.blend

import android.opengl.GLES32
import com.colovaria.graphics.gles.GUniform

enum class BlendMode(private val value: Int) : GUniform {
    NORMAL(0),
    DARKEN(1),
    LIGHTEN(2),
    MULTIPLY(3);

    override fun putUniform(attribLocation: Int) {
        GLES32.glUniform1i(attribLocation, value)
    }
}
package com.colovaria.graphics.uniforms

import com.colovaria.graphics.GUniform
import com.colovaria.graphics.wrappers.GLES

data class GFloat(val value: Float) : GUniform {
    override fun putUniform(attribLocation: Int) {
        GLES.glUniform1f(attribLocation, value)
    }
}
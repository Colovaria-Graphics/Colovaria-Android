package com.colovaria.graphics.gles.uniforms

import com.colovaria.graphics.gles.GLES
import com.colovaria.graphics.gles.GUniform

data class GFloat(val value: Float) : GUniform {
    override fun putUniform(attribLocation: Int) {
        GLES.glUniform1f(attribLocation, value)
    }
}
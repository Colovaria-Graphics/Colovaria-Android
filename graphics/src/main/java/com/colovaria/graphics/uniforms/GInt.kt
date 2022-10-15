package com.colovaria.graphics.uniforms

import com.colovaria.graphics.GUniform
import com.colovaria.graphics.wrappers.GLES

data class GInt(val value: Int) : GUniform {
    override fun putUniform(attribLocation: Int) {
        GLES.glUniform1i(attribLocation, value)
    }
}
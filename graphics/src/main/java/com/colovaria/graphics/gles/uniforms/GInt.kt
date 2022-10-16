package com.colovaria.graphics.gles.uniforms

import com.colovaria.graphics.gles.GLES
import com.colovaria.graphics.gles.GUniform

data class GInt(val value: Int) : GUniform {
    override fun putUniform(attribLocation: Int) {
        GLES.glUniform1i(attribLocation, value)
    }
}
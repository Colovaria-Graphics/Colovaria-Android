package com.colovaria.graphics.gles.uniforms

import com.colovaria.graphics.gles.GLES
import com.colovaria.graphics.gles.GUniform

data class GBool(val value: Boolean) : GUniform {
    override fun putUniform(attribLocation: Int) {
        GLES.glUniform1i(attribLocation, if (value) 1 else 0)
    }
}
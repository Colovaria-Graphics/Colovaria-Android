package com.colovaria.graphics.uniforms

import com.colovaria.graphics.GUniform
import com.colovaria.graphics.wrappers.GLES

data class GBool(val value: Boolean) : GUniform {
    override fun putUniform(attribLocation: Int) {
        GLES.glUniform1i(attribLocation, if (value) 1 else 0)
    }
}
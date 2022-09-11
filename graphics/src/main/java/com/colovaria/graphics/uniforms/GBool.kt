package com.colovaria.graphics.uniforms

import android.opengl.GLES32
import com.colovaria.graphics.GUniform

data class GBool(val value: Boolean) : GUniform {
    override fun putUniform(attribLocation: Int) {
        GLES32.glUniform1i(attribLocation, if (value) 1 else 0)
    }
}
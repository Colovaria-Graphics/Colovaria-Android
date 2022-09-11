package com.graphics.uniforms

import android.opengl.GLES32
import com.graphics.GUniform

data class GInt(val value: Int) : GUniform {
    override fun putUniform(attribLocation: Int) {
        GLES32.glUniform1i(attribLocation, value)
    }
}
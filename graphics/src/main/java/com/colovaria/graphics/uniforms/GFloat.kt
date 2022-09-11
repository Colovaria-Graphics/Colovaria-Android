package com.colovaria.graphics.uniforms

import android.opengl.GLES32
import com.colovaria.graphics.GUniform

data class GFloat(val value: Float) : GUniform {
    override fun putUniform(attribLocation: Int) {
        GLES32.glUniform1f(attribLocation, value)
    }
}
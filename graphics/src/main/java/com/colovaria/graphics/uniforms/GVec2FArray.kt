package com.colovaria.graphics.uniforms

import android.opengl.GLES32
import com.colovaria.geometry.Vector2F
import com.colovaria.graphics.GUniform

data class GVec2FArray(val list: List<Vector2F>) : GUniform {
    private val floatArray = list.flatMap { it.asList() }.toFloatArray()

    override fun putUniform(attribLocation: Int) {
        GLES32.glUniform2fv(attribLocation, list.size, floatArray, 0)
    }
}
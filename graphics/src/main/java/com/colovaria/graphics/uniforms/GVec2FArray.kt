package com.colovaria.graphics.uniforms

import com.colovaria.geometry.Vector2F
import com.colovaria.graphics.GUniform
import com.colovaria.graphics.wrappers.GLES

data class GVec2FArray(val list: List<Vector2F>) : GUniform {
    private val floatArray = list.flatMap { it.asList() }.toFloatArray()

    override fun putUniform(attribLocation: Int) {
        GLES.glUniform2fv(attribLocation, list.size, floatArray, 0)
    }
}
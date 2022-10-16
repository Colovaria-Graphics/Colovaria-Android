package com.colovaria.graphics.gles.uniforms

import com.colovaria.geometry.Vector3F
import com.colovaria.graphics.gles.GLES
import com.colovaria.graphics.gles.GUniform

data class GVec3FArray(val list: List<Vector3F>) : GUniform {
    private val floatArray = list.flatMap { it.asList() }.toFloatArray()

    override fun putUniform(attribLocation: Int) {
        GLES.glUniform3fv(attribLocation, list.size, floatArray, 0)
    }
}
package com.graphics.uniforms

import android.opengl.GLES32
import com.geometry.Vector3F
import com.graphics.GUniform

data class GVec3FArray(val list: List<Vector3F>) : GUniform {
    private val floatArray = list.flatMap { it.asList() }.toFloatArray()

    override fun putUniform(attribLocation: Int) {
        GLES32.glUniform3fv(attribLocation, list.size, floatArray, 0)
    }
}
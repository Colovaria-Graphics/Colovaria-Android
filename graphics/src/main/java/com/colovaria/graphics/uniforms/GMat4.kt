package com.colovaria.graphics.uniforms

import com.colovaria.geometry.Matrix4F
import com.colovaria.graphics.GUniform
import com.colovaria.graphics.wrappers.GLES

data class GMat4(val mat4: Matrix4F) : GUniform {
    override fun putUniform(attribLocation: Int) {
        GLES.glUniformMatrix4fv(attribLocation, 1, false, mat4.mat.toFloatArray(), 0)
    }
}
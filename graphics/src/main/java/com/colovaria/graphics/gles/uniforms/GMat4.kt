package com.colovaria.graphics.gles.uniforms

import com.colovaria.geometry.Matrix4F
import com.colovaria.graphics.gles.GLES
import com.colovaria.graphics.gles.GUniform

data class GMat4(val mat4: Matrix4F) : GUniform {
    override fun putUniform(attribLocation: Int) {
        GLES.glUniformMatrix4fv(attribLocation, 1, false, mat4.mat.toFloatArray(), 0)
    }
}
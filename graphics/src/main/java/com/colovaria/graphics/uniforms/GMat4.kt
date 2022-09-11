package com.colovaria.graphics.uniforms

import android.opengl.GLES32
import com.colovaria.geometry.Matrix4F
import com.colovaria.graphics.GUniform

data class GMat4(val mat4: Matrix4F) : GUniform {
    override fun putUniform(attribLocation: Int) {
        GLES32.glUniformMatrix4fv(attribLocation, 1, false, mat4.mat.toFloatArray(), 0)
    }
}
package com.graphics.uniforms

import android.opengl.GLES32
import com.geometry.Matrix4F
import com.graphics.GUniform

data class GMat4(val mat4: Matrix4F) : GUniform {
    override fun putUniform(attribLocation: Int) {
        GLES32.glUniformMatrix4fv(attribLocation, 1, false, mat4.mat.toFloatArray(), 0)
    }
}
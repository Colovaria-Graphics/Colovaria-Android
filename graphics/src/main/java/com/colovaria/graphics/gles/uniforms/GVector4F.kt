package com.colovaria.graphics.gles.uniforms

import com.colovaria.geometry.Vector4F
import com.colovaria.graphics.gles.GLES
import com.colovaria.graphics.gles.GUniform

data class GVector4F(val x: Float, val y: Float, val z: Float, val w: Float) : GUniform {
    constructor(vector4F: Vector4F) : this(vector4F.x, vector4F.y, vector4F.z, vector4F.w)

    override fun putUniform(attribLocation: Int) {
        GLES.glUniform4f(attribLocation, x, y, z, w)
    }
}
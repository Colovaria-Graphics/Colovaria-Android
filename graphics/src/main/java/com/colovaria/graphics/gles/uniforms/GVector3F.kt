package com.colovaria.graphics.gles.uniforms

import com.colovaria.geometry.Vector3F
import com.colovaria.graphics.gles.GLES
import com.colovaria.graphics.gles.GUniform

data class GVector3F(val x: Float, val y: Float, val z: Float) : GUniform {
    constructor(vector3F: Vector3F) : this(vector3F.x, vector3F.y, vector3F.z)

    override fun putUniform(attribLocation: Int) {
        GLES.glUniform3f(attribLocation, x, y, z)
    }
}
package com.colovaria.graphics.uniforms

import com.colovaria.geometry.Vector2F
import com.colovaria.graphics.GUniform
import com.colovaria.graphics.wrappers.GLES

data class GVector2F(val x: Float, val y: Float) : GUniform {
    constructor(vector2F: Vector2F) : this(vector2F.x, vector2F.y)

    override fun putUniform(attribLocation: Int) {
        GLES.glUniform2f(attribLocation, x, y)
    }
}
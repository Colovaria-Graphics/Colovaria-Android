package com.colovaria.graphics.uniforms

import android.graphics.Color
import android.opengl.GLES32
import com.colovaria.geometry.Vector3F
import com.colovaria.geometry.Vector4F
import com.colovaria.graphics.GUniform

data class GColor(
    val red: Float,
    val green: Float,
    val blue: Float,
    val alpha: Float,
) : GUniform {
    fun asArray() = floatArrayOf(red, green, blue, alpha)

    fun asVec4() = Vector4F(red, green, blue, alpha)

    fun asVec3() = Vector3F(red, green, blue)

    fun asAndroidColor() = Color.valueOf(red, green, blue, alpha)

    override fun putUniform(attribLocation: Int) {
        GLES32.glUniform4f(attribLocation, red, green, blue, alpha)
    }

    companion object {
        val BLACK = GColor(0f, 0f, 0f, 1f)
        val WHITE = GColor(1f, 1f, 1f, 1f)
        val RED = GColor(1f, 0f, 0f, 1f)
        val GREEN = GColor(0f, 1f, 0f, 1f)
        val BLUE = GColor(0f, 0f, 1f, 1f)
        val TRANSPARENT = GColor(0f, 0f, 0f, 0f)
    }
}
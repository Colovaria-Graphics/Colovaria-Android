package com.geometry

data class Vector4F(
    val x: Float,
    val y: Float,
    val z: Float,
    val w: Float,
) {
    fun asArray() = arrayOf(x, y, z, w)

    fun asList() = listOf(x, y, z, w)

    operator fun plus(other: Vector4F) = Vector4F(
        x + other.x,
        y + other.y,
        z + other.z,
        w + other.w,
    )

    operator fun minus(other: Vector4F) = Vector4F(
        x - other.x,
        y - other.y,
        z - other.z,
        w - other.w,
    )

    operator fun times(scalar: Float) = Vector4F(
        x * scalar,
        y * scalar,
        z * scalar,
        w * scalar,
    )
}
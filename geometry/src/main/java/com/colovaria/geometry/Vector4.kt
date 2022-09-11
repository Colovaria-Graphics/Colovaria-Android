package com.colovaria.geometry

data class Vector4(
    val x: Int,
    val y: Int,
    val z: Int,
    val w: Int,
) {
    fun asArray() = arrayOf(x, y, z, w)

    fun asList() = listOf(x, y, z, w)

    operator fun plus(other: Vector4) = Vector4(
        x + other.x,
        y + other.y,
        z + other.z,
        w + other.w,
    )

    operator fun minus(other: Vector4) = Vector4(
        x - other.x,
        y - other.y,
        z - other.z,
        w - other.w,
    )

    operator fun times(scalar: Int) = Vector4(
        x * scalar,
        y * scalar,
        z * scalar,
        w * scalar,
    )
}
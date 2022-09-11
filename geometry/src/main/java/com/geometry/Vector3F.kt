package com.geometry

data class Vector3F(
    val x: Float,
    val y: Float,
    val z: Float,
) {
    fun asArray() = arrayOf(x, y, z)

    fun asList() = listOf(x, y, z)

    operator fun plus(other: Vector3F) = Vector3F(
        x + other.x,
        y + other.y,
        z + other.z,
    )

    operator fun minus(other: Vector3F) = Vector3F(
        x - other.x,
        y - other.y,
        z - other.z,
    )

    operator fun times(scalar: Float) = Vector3F(
        x * scalar,
        y * scalar,
        z * scalar,
    )
}
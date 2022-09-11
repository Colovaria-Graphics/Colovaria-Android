package com.geometry

data class Vector3(
    val x: Int,
    val y: Int,
    val z: Int,
) {
    fun asArray() = arrayOf(x, y, z)

    fun asList() = listOf(x, y, z)

    operator fun plus(other: Vector3) = Vector3(
        x + other.x,
        y + other.y,
        z + other.z,
    )

    operator fun minus(other: Vector3) = Vector3(
        x - other.x,
        y - other.y,
        z - other.z,
    )

    operator fun times(scalar: Int) = Vector3(
        x * scalar,
        y * scalar,
        z * scalar,
    )
}
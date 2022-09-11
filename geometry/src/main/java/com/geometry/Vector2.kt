package com.geometry

data class Vector2(
    val x: Int,
    val y: Int,
) {
    fun asArray() = arrayOf(x, y)

    fun asList() = listOf(x, y)

    operator fun plus(other: Vector2) = Vector2(
        x + other.x,
        y + other.y,
    )

    operator fun minus(other: Vector2) = Vector2(
        x - other.x,
        y - other.y,
    )

    operator fun times(scalar: Int) = Vector2(
        x * scalar,
        y * scalar,
    )
}
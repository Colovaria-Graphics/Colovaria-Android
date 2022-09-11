package com.geometry

import kotlin.math.roundToInt

data class Vector2F(
    val x: Float,
    val y: Float,
) {
    fun asArray() = arrayOf(x, y)

    fun asList() = listOf(x, y)

    operator fun plus(other: Vector2F) = Vector2F(
        x + other.x,
        y + other.y,
    )

    operator fun minus(other: Vector2F) = Vector2F(
        x - other.x,
        y - other.y,
    )

    operator fun times(scalar: Float) = Vector2F(
        x * scalar,
        y * scalar,
    )

    operator fun times(size: Size) = Vector2(
        (x * size.width).roundToInt(),
        (y * size.height).roundToInt(),
    )

    operator fun div(other: Vector2F) = Vector2F(
        x / other.x,
        y / other.y,
    )

    operator fun div(other: SizeF) = Vector2F(
        x / other.width,
        y / other.height,
    )

    operator fun div(other: Size) = Vector2F(
        x / other.width,
        y / other.height,
    )

    operator fun div(scalar: Float) = Vector2F(
        x / scalar,
        y / scalar,
    )
}
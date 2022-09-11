package com.geometry

import kotlin.math.*

data class SizeF(
    val width: Float,
    val height: Float,
) {
    operator fun times(size: Size) = SizeF(
        width * size.width,
        height * size.height
    )

    operator fun times(size: SizeF) = SizeF(
        width * size.width,
        height * size.height
    )

    operator fun times(vector: Vector2F) = SizeF(
        width * vector.x,
        height * vector.y
    )

    operator fun times(scalar: Float) = SizeF(
        width * scalar,
        height * scalar
    )

    operator fun div(other: SizeF) = SizeF(
        width / other.width,
        height / other.height,
    )

    operator fun div(scalar: Float) = SizeF(
        width / scalar,
        height / scalar,
    )

    fun roundToSize() = Size(
        width.roundToInt(),
        height.roundToInt(),
    )

    fun floorToSize() = Size(
        floor(width).roundToInt(),
        floor(height).roundToInt(),
    )

    fun ceilToSize() = Size(
        ceil(width).roundToInt(),
        ceil(height).roundToInt(),
    )

    fun transpose() = SizeF(
        height,
        width,
    )

    fun minVertex() = min(width, height)

    fun pow(power: Int) = SizeF(
        width.pow(power),
        height.pow(power),
    )

    fun pow(power: Float) = SizeF(
        width.pow(power),
        height.pow(power),
    )
}
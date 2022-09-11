package com.geometry

data class Size(
    val width: Int,
    val height: Int,
) {
    operator fun times(size: Size) = Size(
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

    operator fun div(other: Size) = SizeF(
        width.toFloat() / other.width.toFloat(),
        height.toFloat() / other.height.toFloat(),
    )

    operator fun div(scalar: Float) = SizeF(
        width.toFloat() / scalar,
        height.toFloat() / scalar,
    )

    operator fun minus(other: Vector2) = Size(
        width - other.x,
        height - other.y,
    )

    operator fun minus(scalar: Int) = Size(
        width - scalar,
        height - scalar,
    )

    fun toSizeF() = SizeF(
        width.toFloat(),
        height.toFloat(),
    )
}
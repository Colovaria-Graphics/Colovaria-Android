package com.colovaria.geometry

data class RectF(
    val center: Vector2F,
    val size: SizeF
) {
    val width = size.width
    val height = size.height

    val left = center.x - size.width / 2
    val right = center.x + size.width / 2
    val top = center.y + size.height / 2
    val bottom = center.y - size.height / 2

    constructor(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float
    ) : this(Vector2F(
        left + (right - left) / 2f,
        bottom + (top - bottom) / 2f
    ), SizeF(
        right - left,
        top - bottom
    ))

    operator fun times(size: Size) : Rect {
        return Rect(center * size, (this.size * size).roundToSize())
    }

    fun clampTo(container: RectF) = RectF(
        left.coerceIn(container.left, container.right),
        top.coerceIn(container.bottom, container.top),
        right.coerceIn(container.left, container.right),
        bottom.coerceIn(container.bottom, container.top)
    )
}
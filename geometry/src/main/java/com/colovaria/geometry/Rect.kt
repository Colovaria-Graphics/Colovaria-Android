package com.colovaria.geometry

data class Rect(
    val center: Vector2,
    val size: Size
) {
    val width = size.width
    val height = size.height

    val left = center.x - size.width / 2
    val right = center.x + size.width / 2
    val top = center.y + size.height / 2
    val bottom = center.y - size.height / 2

    constructor(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) : this(Vector2(
        left + (right - left) / 2,
        bottom + (top - bottom) / 2
    ), Size(
        right - left,
        top - bottom
    ))

    fun clampTo(container: Rect) = Rect(
        left.coerceIn(container.left, container.right),
        top.coerceIn(container.bottom, container.top),
        right.coerceIn(container.left, container.right),
        bottom.coerceIn(container.bottom, container.top)
    )
}
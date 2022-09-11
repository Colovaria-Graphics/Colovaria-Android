package com.geometry

import kotlin.math.roundToInt

enum class AdjustMode {
    ASPECT_FIT,
    ASPECT_FILL,
    FILL,
    ORIGINAL;

    fun targetSize(contentSize: Size, containerSize: Size) : Size = when (this) {
        ASPECT_FIT -> {
            val widthRatio = containerSize.width.toFloat() / contentSize.width.toFloat()
            val heightRatio = containerSize.height.toFloat() / contentSize.height.toFloat()

            if (widthRatio >= heightRatio) {
                Size((heightRatio * contentSize.width).roundToInt(), containerSize.height)
            } else {
                Size(containerSize.width, (widthRatio * contentSize.height).roundToInt())
            }
        }
        ASPECT_FILL -> {
            val widthRatio = containerSize.width.toFloat() / contentSize.width.toFloat()
            val heightRatio = containerSize.height.toFloat() / contentSize.height.toFloat()

            if (widthRatio <= heightRatio) {
                Size((heightRatio * contentSize.width).roundToInt(), containerSize.height)
            } else {
                Size(containerSize.width, (widthRatio * contentSize.height).roundToInt())
            }
        }
        FILL -> containerSize
        ORIGINAL -> contentSize
    }
}
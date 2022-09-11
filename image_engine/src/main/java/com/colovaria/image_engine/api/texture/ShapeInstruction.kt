package com.colovaria.image_engine.api.texture

import com.colovaria.geometry.SizeF
import com.colovaria.graphics.uniforms.GColor

sealed class ShapeInstruction : TextureInstruction()

data class RectInstruction(
    val size: SizeF,
    val fill: GColor,
) : ShapeInstruction()

data class CircleInstruction(
    val size: SizeF,
    val fill: GColor,
) : ShapeInstruction()
package com.image_engine.api.texture

import com.geometry.SizeF
import com.graphics.uniforms.GColor

sealed class ShapeInstruction : TextureInstruction()

data class RectInstruction(
    val size: SizeF,
    val fill: GColor,
) : ShapeInstruction()

data class CircleInstruction(
    val size: SizeF,
    val fill: GColor,
) : ShapeInstruction()
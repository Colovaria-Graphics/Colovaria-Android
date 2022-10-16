package com.colovaria.image_engine.api.texture

import com.colovaria.geometry.SizeF
import com.colovaria.graphics.gles.uniforms.GColor
import com.colovaria.image_engine.api.texture.base.DrawerInstruction

sealed class ShapeInstruction : DrawerInstruction

data class RectInstruction(
    val size: SizeF,
    val fill: GColor,
) : ShapeInstruction()

data class CircleInstruction(
    val size: SizeF,
    val fill: GColor,
) : ShapeInstruction()

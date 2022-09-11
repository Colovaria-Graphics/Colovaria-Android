package com.colovaria.image_engine.api.blend

import com.colovaria.geometry.AdjustMode
import com.colovaria.geometry.Vector2F

data class BlenderInstruction(
    val blendMode: BlendMode = BlendMode.NORMAL,
    val center: Vector2F = Vector2F(0.5f, 0.5f),
    val scale: Vector2F = Vector2F(1f, 1f),
    val opacity: Float = 1f,
    val adjustMode: AdjustMode = AdjustMode.ASPECT_FIT,
)
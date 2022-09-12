package com.colovaria.image_engine.api.texture

import com.colovaria.geometry.Vector2F
import com.colovaria.graphics.uniforms.GColor

data class Gradient2DInstruction(
    val colors: Map<Vector2F, GColor>,
) : TextureInstruction()
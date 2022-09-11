package com.image_engine.api.texture

import com.geometry.Vector2F
import com.graphics.uniforms.GColor

data class Gradient2DInstruction(
    val colors: Map<Vector2F, GColor>,
) : TextureInstruction()
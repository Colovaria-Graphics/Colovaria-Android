package com.image_engine.api

import com.graphics.uniforms.GColor

data class Frame(
    val layers: List<Layer> = emptyList(),
    val backgroundColor: GColor = GColor.TRANSPARENT,
)
package com.colovaria.image_engine.api

import com.colovaria.graphics.gles.uniforms.GColor

data class Frame(
    val layers: List<Layer> = emptyList(),
    val backgroundColor: GColor = GColor.TRANSPARENT,
)
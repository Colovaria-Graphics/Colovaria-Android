package com.colovaria.image_engine.api.texture

import com.colovaria.image_engine.api.texture.base.ProcessorInstruction

data class HSVInstruction(
    val hueFactor: Float,
    val saturationFactor: Float,
    val valueFactor: Float,
) : ProcessorInstruction

package com.colovaria.image_engine.api.texture

data class HSVInstruction(
    val hueFactor: Float,
    val saturationFactor: Float,
    val valueFactor: Float,
) : ProcessorInstruction()
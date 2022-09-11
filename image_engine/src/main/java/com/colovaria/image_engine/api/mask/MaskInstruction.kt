package com.colovaria.image_engine.api.mask

import com.colovaria.image_engine.api.Layer

data class MaskInstruction(
    val type: MaskType,
    val mask: Layer,
)
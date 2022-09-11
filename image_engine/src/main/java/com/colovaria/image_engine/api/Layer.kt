package com.colovaria.image_engine.api

import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.mask.MaskInstruction
import com.colovaria.image_engine.api.texture.TextureInstruction

data class Layer(
    val texturing: TextureInstruction,
    val blending: BlenderInstruction = BlenderInstruction(),
    val masking: MaskInstruction? = null,
)
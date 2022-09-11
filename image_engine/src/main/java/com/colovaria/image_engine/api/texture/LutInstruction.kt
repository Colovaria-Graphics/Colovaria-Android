package com.colovaria.image_engine.api.texture

import com.colovaria.image_engine.api.resources.ResourcePath

data class LutInstruction(
    val path: ResourcePath,
) : TextureInstruction()
package com.image_engine.api.texture

import com.image_engine.api.resources.ResourcePath

data class LutInstruction(
    val path: ResourcePath,
) : TextureInstruction()
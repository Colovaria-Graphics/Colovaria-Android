package com.image_engine.api.texture

import com.geometry.RectF
import com.image_engine.api.resources.ResourcePath

data class ImageInstruction(
    val path: ResourcePath,
    val crop: RectF = RectF(0f, 1f, 1f, 0f),
) : TextureInstruction()
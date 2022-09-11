package com.image_engine.api.texture

import com.image_engine.api.Frame

data class GroupInstruction(
    val frame: Frame
) : TextureInstruction()
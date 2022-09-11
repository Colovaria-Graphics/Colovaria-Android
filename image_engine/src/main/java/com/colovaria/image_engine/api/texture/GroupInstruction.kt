package com.colovaria.image_engine.api.texture

import com.colovaria.image_engine.api.Frame

data class GroupInstruction(
    val frame: Frame
) : TextureInstruction()
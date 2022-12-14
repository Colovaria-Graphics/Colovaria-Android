package com.colovaria.image_engine.api.texture

import com.colovaria.image_engine.api.Frame
import com.colovaria.image_engine.api.texture.base.DrawerInstruction

data class GroupInstruction(
    val frame: Frame
) : DrawerInstruction

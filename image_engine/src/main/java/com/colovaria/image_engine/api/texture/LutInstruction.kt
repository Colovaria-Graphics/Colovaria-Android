package com.colovaria.image_engine.api.texture

import com.colovaria.image_engine.api.resources.ResourcePath
import com.colovaria.image_engine.api.texture.base.DrawerInstruction

data class LutInstruction(
    val path: ResourcePath,
) : DrawerInstruction

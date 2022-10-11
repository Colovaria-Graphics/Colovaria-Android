package com.colovaria.image_engine.api.texture

import com.colovaria.geometry.RectF
import com.colovaria.image_engine.api.resources.ResourcePath
import com.colovaria.image_engine.api.texture.base.DrawerInstruction

data class ImageInstruction(
    val path: ResourcePath,
    val crop: RectF = RectF(0f, 1f, 1f, 0f),
) : DrawerInstruction

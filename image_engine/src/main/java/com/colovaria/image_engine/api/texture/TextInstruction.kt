package com.colovaria.image_engine.api.texture

import com.colovaria.graphics.gles.uniforms.GColor
import com.colovaria.image_engine.api.text.Font
import com.colovaria.image_engine.api.text.TextAlignment
import com.colovaria.image_engine.api.text.TextDirection
import com.colovaria.image_engine.api.texture.base.DrawerInstruction

data class TextInstruction(
    val text: String,
    val size: Float,
    val color: GColor,
    val bold: Boolean,
    val italic: Boolean,
    val font: Font,
    val spacing: Float,
    val lineSpacing: Float,
    val maxLines: Int = 0,
    val direction: TextDirection,
    val alignment: TextAlignment,
    val maxWidth: Float
) : DrawerInstruction

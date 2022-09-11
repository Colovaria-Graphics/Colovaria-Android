package com.colovaria.image_engine.pipeline.drawers

import android.content.Context
import com.colovaria.geometry.Size
import com.colovaria.graphics.GTexture
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.resources.ImageLoader
import com.colovaria.image_engine.api.texture.*
import com.colovaria.image_engine.text.FontProvider
import com.colovaria.image_engine.text.TextMeasurer

class CompositeTextureDrawer(
    context: Context,
    imageLoader: ImageLoader,
    textMeasurer: TextMeasurer,
    fontProvider: FontProvider,
    val size: Size,
    optimizeImageSize: Boolean
) : TextureDrawer<TextureInstruction>(false) {

    private val imageDrawer = ImageDrawer(imageLoader, size, optimizeImageSize)
    private val gradient2DDrawer = Gradient2DDrawer(context, size)
    private val shapeDrawer = ShapeDrawer(size)
    private val textDrawer = TextDrawer(textMeasurer, fontProvider, size)

    private val drawers = listOf(imageDrawer, gradient2DDrawer, shapeDrawer, textDrawer)

    override fun preFrameDraw() {
        super.preFrameDraw()
        drawers.forEach { it.preFrameDraw() }
    }

    override fun drawInternal(instruction: TextureInstruction, blending: BlenderInstruction) : GTexture {
        return when (instruction) {
            is ImageInstruction -> imageDrawer.draw(instruction, blending)
            is Gradient2DInstruction -> gradient2DDrawer.draw(instruction, blending)
            is ShapeInstruction -> shapeDrawer.draw(instruction, blending)
            is TextInstruction -> textDrawer.draw(instruction, blending)
            else -> error("Unsupported instruction: $instruction")
        }
    }

    override fun postFrameDraw() {
        super.postFrameDraw()
        drawers.forEach { it.postFrameDraw() }
    }

    override fun dispose() {
        super.dispose()
        drawers.forEach { it.dispose() }
    }
}
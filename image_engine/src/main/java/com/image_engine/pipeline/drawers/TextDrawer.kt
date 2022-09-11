package com.image_engine.pipeline.drawers

import android.graphics.Canvas
import android.graphics.Color
import com.geometry.Size
import com.graphics.GTexture
import com.image_engine.api.blend.BlenderInstruction
import com.image_engine.api.texture.TextInstruction
import com.image_engine.text.CanvasTextFactory
import com.image_engine.text.FontProvider
import com.image_engine.text.TextMeasurer
import com.image_engine.utils.BitmapsLruCache

class TextDrawer(
    private val textMeasurer: TextMeasurer,
    private val fontProvider: FontProvider,
    private val size: Size,
) : TextureDrawer<TextInstruction>() {
    private val bitmapCache = BitmapsLruCache()

    override fun drawInternal(
        instruction: TextInstruction,
        blending: BlenderInstruction
    ): GTexture {
        val targetSize = textMeasurer.measure(instruction, size)
        val bitmap = bitmapCache.computeIfAbsent(targetSize)
        val canvas = Canvas(bitmap)

        bitmap.eraseColor(Color.TRANSPARENT)
        CanvasTextFactory.createStaticLayout(instruction, size, fontProvider).draw(canvas)

        return GTexture(bitmap)
    }

    override fun dispose() {
        super.dispose()
        bitmapCache.evictAll()
    }
}
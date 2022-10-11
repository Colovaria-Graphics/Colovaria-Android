package com.colovaria.image_engine.pipeline.drawers

import android.graphics.Canvas
import android.graphics.Color
import com.colovaria.geometry.Size
import com.colovaria.graphics.GTexture
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.texture.TextInstruction
import com.colovaria.image_engine.pipeline.drawers.base.CachedTextureDrawer
import com.colovaria.image_engine.text.CanvasTextFactory
import com.colovaria.image_engine.text.FontProvider
import com.colovaria.image_engine.text.TextMeasurer
import com.colovaria.image_engine.utils.BitmapsLruCache

class TextDrawer(
    private val textMeasurer: TextMeasurer,
    private val fontProvider: FontProvider,
    private val size: Size,
) : CachedTextureDrawer<TextInstruction>() {
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
package com.colovaria.image_engine.pipeline.drawers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.colovaria.geometry.Size
import com.colovaria.graphics.gles.GTexture
import com.colovaria.graphics.gles.uniforms.GColor
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.texture.CircleInstruction
import com.colovaria.image_engine.api.texture.RectInstruction
import com.colovaria.image_engine.api.texture.ShapeInstruction
import com.colovaria.image_engine.pipeline.drawers.base.CachedTextureDrawer
import com.colovaria.image_engine.utils.BitmapsLruCache

class ShapeDrawer(
    private val size : Size,
) : CachedTextureDrawer<ShapeInstruction>() {
    private val bitmapCache = BitmapsLruCache()

    override fun drawInternal(
        instruction: ShapeInstruction,
        blending: BlenderInstruction
    ): GTexture {
        return GTexture(when (instruction) {
            is RectInstruction -> drawRect(instruction)
            is CircleInstruction -> drawCircle(instruction)
        })
    }

    override fun dispose() {
        super.dispose()
        bitmapCache.evictAll()
    }

    private fun drawRect(rectInstruction: RectInstruction) : Bitmap {
        val targetSize = (rectInstruction.size * size).ceilToSize()
        val bitmap = bitmapCache.computeIfAbsent(targetSize)
        val canvas = Canvas(bitmap)

        canvas.drawColor(Color.TRANSPARENT)
        canvas.drawRect(0f, 0f, targetSize.width.toFloat(), targetSize.height.toFloat(),
            createPaint(rectInstruction.fill))

        return bitmap
    }

    private fun drawCircle(circleInstruction: CircleInstruction) : Bitmap {
        val targetSize = (circleInstruction.size * size).roundToSize()
        val bitmap = bitmapCache.computeIfAbsent(targetSize)
        val canvas = Canvas(bitmap)

        canvas.drawColor(Color.TRANSPARENT)
        canvas.drawOval(0f, 0f, targetSize.width.toFloat(), targetSize.height.toFloat(),
            createPaint(circleInstruction.fill))

        return bitmap
    }

    private fun createPaint(color: GColor) = Paint().apply {
        setColor(color.asAndroidColor().toArgb())
        isAntiAlias = true
    }
}
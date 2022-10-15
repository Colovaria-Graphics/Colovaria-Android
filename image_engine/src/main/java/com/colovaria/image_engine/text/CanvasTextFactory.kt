package com.colovaria.image_engine.text

import android.graphics.Paint
import android.graphics.text.LineBreaker
import android.os.Build
import android.text.StaticLayout
import android.text.TextPaint
import com.colovaria.geometry.Size
import com.colovaria.image_engine.api.texture.TextInstruction
import kotlin.math.roundToInt

object CanvasTextFactory {
    fun createStaticLayout(
        instruction: TextInstruction,
        canvasSize: Size,
        fontProvider: FontProvider,
        antiAlias: Boolean = true
    ) : StaticLayout {
        return createStaticLayout(instruction, canvasSize, createPaint(instruction, canvasSize, fontProvider, antiAlias))
    }

    fun createPaint(
        instruction: TextInstruction,
        canvasSize: Size,
        fontProvider: FontProvider,
        antiAlias: Boolean = true
    ) : TextPaint {
        return TextPaint().apply {
            isAntiAlias = antiAlias
            letterSpacing = instruction.spacing * canvasSize.width
            textSize = instruction.size * canvasSize.height
            color = instruction.color.asAndroidColor().toArgb()
            strokeWidth = 0f
            style = Paint.Style.FILL_AND_STROKE
            typeface = fontProvider.provide(instruction.font, instruction.bold, instruction.italic)
            setShadowLayer(0f, 0f, 0f, 0)
        }
    }

    fun createStaticLayout(instruction: TextInstruction, canvasSize: Size, paint: TextPaint) : StaticLayout {
        return StaticLayout.Builder.obtain(
            instruction.text,
            0,
            instruction.text.length,
            paint,
            (instruction.maxWidth * canvasSize.width).roundToInt()
        ).apply {
            setLineSpacing(instruction.lineSpacing * canvasSize.height, 1f)
            setMaxLines(instruction.maxLines)
            setTextDirection(instruction.direction.toAndroidDirection())
            setAlignment(instruction.alignment.toAndroidAlignment())
            setIncludePad(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setBreakStrategy(LineBreaker.BREAK_STRATEGY_BALANCED)
            }
        }.build()
    }
}
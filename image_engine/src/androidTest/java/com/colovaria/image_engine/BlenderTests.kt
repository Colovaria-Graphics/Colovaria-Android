package com.colovaria.image_engine

import com.colovaria.geometry.AdjustMode
import com.colovaria.geometry.Size
import com.colovaria.geometry.SizeF
import com.colovaria.graphics.GFrameBuffer
import com.colovaria.graphics.GTexture
import com.colovaria.graphics.egl.GContext
import com.colovaria.graphics.egl.GDisplay
import com.colovaria.graphics.egl.GSurface
import com.colovaria.graphics.uniforms.GColor
import com.colovaria.graphics.withBind
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.texture.CircleInstruction
import com.colovaria.image_engine.api.texture.RectInstruction
import com.colovaria.image_engine.pipeline.drawers.ShapeDrawer
import com.colovaria.image_engine.utils.BitmapUtils
import com.colovaria.image_engine.utils.TestClass
import org.junit.After
import org.junit.Before
import org.junit.Test

class BlenderTests : TestClass() {
    private val blender by lazy { Blender(context, FAKE_CANVAS_SIZE) }
    private val shapeDrawer by lazy { ShapeDrawer(FAKE_CANVAS_SIZE) }

    @Before
    fun setup() {
        val display = GDisplay.create()
        val gpuContext = GContext.create(display)
        val surface = GSurface.create(display, FAKE_CANVAS_SIZE)
        gpuContext.bind(surface)
    }

    @Test
    fun Blender_DrawCircleOverRect_ShouldByAsExpected() {
        val backTexture = generateRectTexture(SizeF(1f, 1f), GColor.GREEN)
        val frontTexture = generateCircleTexture(SizeF(0.5f, 0.5f), GColor.RED)

        val frameBuffer = GFrameBuffer(FAKE_CANVAS_SIZE)
        frameBuffer.withBind {
            blender.blend(
                backTexture,
                frontTexture,
                BlenderInstruction(adjustMode = AdjustMode.ORIGINAL)
            )
        }

        BitmapUtils.assertBitmapEqual(frameBuffer.save(), "test_results/blender/test_1_result.png")
    }

    @After
    fun cleanup() {
        blender.dispose()
    }

    private fun generateRectTexture(size: SizeF, fill: GColor) : GTexture {
        return shapeDrawer.draw(RectInstruction(size, fill), BlenderInstruction())
    }

    private fun generateCircleTexture(size: SizeF, fill: GColor) : GTexture {
        return shapeDrawer.draw(CircleInstruction(size, fill), BlenderInstruction())
    }

    companion object {
        private val FAKE_CANVAS_SIZE = Size(512, 512)
    }
}
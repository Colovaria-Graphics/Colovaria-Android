package com.colovaria.image_engine.com.colovaria.image_engine.pipeline.drawers

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.colovaria.geometry.Size
import com.colovaria.geometry.Vector2F
import com.colovaria.graphics.egl.GContext
import com.colovaria.graphics.egl.GDisplay
import com.colovaria.graphics.egl.GSurface
import com.colovaria.graphics.uniforms.GColor
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.texture.Gradient2DInstruction
import com.colovaria.image_engine.pipeline.drawers.Gradient2DDrawer
import com.colovaria.image_engine.utils.BitmapUtils
import com.colovaria.image_engine.utils.TestClass
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Gradient2DDrawerTest : TestClass() {
    private val gradientDrawer by lazy { Gradient2DDrawer(context, FAKE_CANVAS_SIZE) }

    @Before
    fun setup() {
        val display = GDisplay.create()
        val gpuContext = GContext.create(display)
        val surface = GSurface.create(display, FAKE_CANVAS_SIZE)
        gpuContext.bind(surface)
    }

    @Test
    fun Gradient2DDrawer_DrawGradient_SouldBeAsExpected() {
        val resultTexture = gradientDrawer.draw(Gradient2DInstruction(mapOf(
            Vector2F(0f, 0f) to GColor.GREEN
        )), BlenderInstruction())

        BitmapUtils.assertBitmapEqual(resultTexture.save(), "test_results/gradient_2d_drawer/test_1_result.png")
    }

    @Test
    fun Gradient2DDrawer_DrawGradient1_SouldBeAsExpected() {
        val resultTexture = gradientDrawer.draw(Gradient2DInstruction(mapOf(
            Vector2F(1f, 1f) to GColor.BLUE,
            Vector2F(0f, 0f) to GColor.GREEN,
        )), BlenderInstruction())

        BitmapUtils.assertBitmapEqual(resultTexture.save(), "test_results/gradient_2d_drawer/test_2_result.png")
    }

    @Test
    fun Gradient2DDrawer_DrawGradient2_SouldBeAsExpected() {
        val resultTexture = gradientDrawer.draw(Gradient2DInstruction(mapOf(
            Vector2F(0f, 0f) to GColor(0.1f, 0.1f, 0.0f, 1.0f),
            Vector2F(1f, 1f) to GColor(0.4f, 0.7f, 0.2f, 1.0f),
            Vector2F(1f, 0f) to GColor(0.8f, 0.3f, 0.8f, 1.0f),
            Vector2F(0f, 1f) to GColor(0.5f, 0.8f, 1.0f, 1.0f)
        )), BlenderInstruction())

        BitmapUtils.assertBitmapEqual(resultTexture.save(), "test_results/gradient_2d_drawer/test_3_result.png")
    }

    @Test
    fun Gradient2DDrawer_DrawGradient3_SouldBeAsExpected() {
        val resultTexture = gradientDrawer.draw(Gradient2DInstruction(mapOf(
            Vector2F(0f, 0f) to GColor(0.1f, 0.2f, 0.4f, 1.0f),
            Vector2F(1f, 1f) to GColor(0.4f, 0.7f, 0.2f, 1.0f),
            Vector2F(1f, 0f) to GColor(0.8f, 0.3f, 0.8f, 1.0f),
            Vector2F(0f, 1f) to GColor(0.5f, 0.8f, 1.0f, 1.0f),
            Vector2F(0.5f, 1f) to GColor(0.2f, 1.0f, 1.0f, 1.0f),
            Vector2F(0.5f, 0.5f) to GColor(0.5f, 0.7f, 0.35f, 1.0f)
        )), BlenderInstruction())

        BitmapUtils.assertBitmapEqual(resultTexture.save(), "test_results/gradient_2d_drawer/test_4_result.png")
    }

    @After
    fun cleanup() {
        gradientDrawer.dispose()
    }

    companion object {
        private val FAKE_CANVAS_SIZE = Size(512, 512)
    }
}
package com.colovaria.image_engine.pipeline.drawers

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.colovaria.geometry.Size
import com.colovaria.geometry.SizeF
import com.colovaria.graphics.egl.GContext
import com.colovaria.graphics.egl.GDisplay
import com.colovaria.graphics.egl.GSurface
import com.colovaria.graphics.uniforms.GColor
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.texture.CircleInstruction
import com.colovaria.image_engine.api.texture.RectInstruction
import com.colovaria.image_engine.utils.BitmapUtils
import com.colovaria.image_engine.utils.TestClass
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShapeDrawerTest : TestClass() {
    private val shapeDrawer by lazy { ShapeDrawer(FAKE_CANVAS_SIZE) }

    @Before
    fun setup() {
        val display = GDisplay.create()
        val gpuContext = GContext.create(display)
        val surface = GSurface.create(display, Size(1, 1))
        gpuContext.bind(surface)
    }

    @Test
    fun ShapeDrawer_DrawRect_SouldBeAsExpected() {
        val resultTexture = shapeDrawer.draw(
            RectInstruction(SizeF(0.5f, 0.5f), GColor.GREEN), BlenderInstruction()
        )

        BitmapUtils.assertBitmapEqual(resultTexture.save(), "test_results/shape_drawer/test_1_result.png")
    }

    @Test
    fun ShapeDrawer_DrawRect2_SouldBeAsExpected() {
        val resultTexture = shapeDrawer.draw(
            RectInstruction(SizeF(0.1f, 0.6f), GColor.BLUE), BlenderInstruction()
        )

        BitmapUtils.assertBitmapEqual(resultTexture.save(), "test_results/shape_drawer/test_2_result.png")
    }

    @Test
    fun ShapeDrawer_DrawCircle_SouldBeAsExpected() {
        val resultTexture = shapeDrawer.draw(
            CircleInstruction(
                SizeF(0.3f, 0.3f),
                GColor(0.5f, 0.1f, 0.6f, 1.0f)
            ),
            BlenderInstruction()
        )

        BitmapUtils.assertBitmapEqual(resultTexture.save(), "test_results/shape_drawer/test_3_result.png")
    }

    @Test
    fun ShapeDrawer_DrawCircle2_SouldBeAsExpected() {
        val resultTexture = shapeDrawer.draw(
            CircleInstruction(
                SizeF(1f, 1f),
                GColor(0.0f, 0.8f, 0.85f, 1.0f)
            ),
            BlenderInstruction()
        )

        BitmapUtils.assertBitmapEqual(resultTexture.save(), "test_results/shape_drawer/test_4_result.png")
    }

    @After
    fun cleanup() {
        shapeDrawer.dispose()
    }

    companion object {
        private val FAKE_CANVAS_SIZE = Size(512, 512)
    }
}
package com.colovaria.image_engine.pipeline.pipeline

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.colovaria.geometry.Size
import com.colovaria.graphics.egl.GContext
import com.colovaria.graphics.egl.GDisplay
import com.colovaria.graphics.egl.GSurface
import com.colovaria.graphics.gles.GTexture
import com.colovaria.image_engine.api.texture.HSVInstruction
import com.colovaria.image_engine.pipeline.processors.HSVProcessor
import com.colovaria.image_engine.utils.BitmapUtils
import com.colovaria.image_engine.utils.TestClass
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HSVProcessorTest : TestClass() {
    private val hsvProcessor by lazy { HSVProcessor(context, FAKE_CANVAS_SIZE) }

    @Before
    fun setup() {
        val display = GDisplay.create()
        val gpuContext = GContext.create(display)
        val surface = GSurface.create(display, FAKE_CANVAS_SIZE)
        gpuContext.bind(surface)
    }

    @Test
    fun HSVProcessor_ProcessImage_SouldBeAsExpected() {
        val texture = GTexture(BitmapUtils.loadBitmapFromAssetPath("test_results/hsv_processor/test_image.jpg"))
        val resultTexture = hsvProcessor.process(
            HSVInstruction(1.0f, 1.0f, 1.0f), texture
        )

        BitmapUtils.assertBitmapEqual(resultTexture.save(), "test_results/hsv_processor/test_1_result.png")
    }

    @Test
    fun HSVProcessor_ProcessImage1_SouldBeAsExpected() {
        val texture = GTexture(BitmapUtils.loadBitmapFromAssetPath("test_results/hsv_processor/test_image.jpg"))
        val resultTexture = hsvProcessor.process(
            HSVInstruction(0.5f, 1.0f, 0.8f), texture
        )

        BitmapUtils.assertBitmapEqual(resultTexture.save(), "test_results/hsv_processor/test_2_result.png")
    }

    @Test
    fun HSVProcessor_ProcessImage2_SouldBeAsExpected() {
        val texture = GTexture(BitmapUtils.loadBitmapFromAssetPath("test_results/hsv_processor/test_image.jpg"))
        val resultTexture = hsvProcessor.process(
            HSVInstruction(0.8f, 0.2f, 1.0f), texture
        )

        BitmapUtils.assertBitmapEqual(resultTexture.save(), "test_results/hsv_processor/test_3_result.png")
    }


    @After
    fun cleanup() {
        hsvProcessor.dispose()
    }

    companion object {
        private val FAKE_CANVAS_SIZE = Size(512, 512)
    }
}

package com.colovaria.image_engine.com.colovaria.image_engine.pipeline.drawers

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.colovaria.geometry.Size
import com.colovaria.graphics.egl.GContext
import com.colovaria.graphics.egl.GDisplay
import com.colovaria.graphics.egl.GSurface
import com.colovaria.graphics.uniforms.GColor
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.resources.AssetResourcePath
import com.colovaria.image_engine.api.text.Font
import com.colovaria.image_engine.api.text.TextAlignment
import com.colovaria.image_engine.api.text.TextDirection
import com.colovaria.image_engine.api.texture.TextInstruction
import com.colovaria.image_engine.pipeline.drawers.TextDrawer
import com.colovaria.image_engine.text.FontProvider
import com.colovaria.image_engine.text.TextMeasurer
import com.colovaria.image_engine.utils.BitmapUtils.assertBitmapEqual
import com.colovaria.image_engine.utils.TestClass
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestTextDrawer : TestClass() {
    private val fontProvider by lazy { FontProvider(context) }
    private val textMeasurer by lazy { TextMeasurer(fontProvider) }
    private val textDrawer by lazy { TextDrawer(textMeasurer, fontProvider, FAKE_CANVAS_SIZE) }

    @Before
    fun setup() {
        val display = GDisplay.create()
        val gpuContext = GContext.create(display)
        val surface = GSurface.create(display, Size(1, 1))
        gpuContext.bind(surface)
    }

    @Test
    fun TextureDrawer_DrawSimpleTextLTRCenter_ShouldByAsExpected() {
        val resultTexture = textDrawer.draw(TextInstruction(
            text = "This is a test",
            size = 0.3f,
            color = GColor.RED,
            bold = true,
            italic = false,
            font = Font(AssetResourcePath(TEST_FONT_1_PATH)),
            spacing = 0.0001f,
            lineSpacing = 0.01f,
            direction = TextDirection.LTR,
            alignment = TextAlignment.CENTER,
            maxWidth = 1f
        ), BlenderInstruction())

        assertBitmapEqual(resultTexture.save(), "test_results/text_drawer/test_1_result.png")
    }

    @Test
    fun TextureDrawer_RTLNormal_ShouldByAsExpected() {
        val resultTexture = textDrawer.draw(TextInstruction(
            text = "ThisIsALongTextWithoutSpaces",
            size = 0.2f,
            color = GColor(0.4f, 0.6f, 0.1f, 0.9f),
            bold = false,
            italic = true,
            font = Font(AssetResourcePath(TEST_FONT_1_PATH)),
            spacing = 0.00001f,
            lineSpacing = 0.005f,
            direction = TextDirection.RTL,
            alignment = TextAlignment.NORMAL,
            maxWidth = 0.8f
        ), BlenderInstruction())

        assertBitmapEqual(resultTexture.save(), "test_results/text_drawer/test_2_result.png")
    }

    @Test
    fun TextureDrawer_DrawRTLText_ShouldByAsExpected() {
        val resultTexture = textDrawer.draw(TextInstruction(
            text = "שלום שלום זה טקסט ממש ממש ממש ממש ממש ארוך בשפה מימן לשמאל",
            size = 0.1f,
            color = GColor(0.8f, 0.2f, 0.5f, 0.9f),
            bold = false,
            italic = false,
            font = Font(AssetResourcePath(TEST_FONT_2_PATH)),
            spacing = 0.0001f,
            lineSpacing = 0.001f,
            direction = TextDirection.RTL,
            alignment = TextAlignment.NORMAL,
            maxWidth = 1f
        ), BlenderInstruction())

        assertBitmapEqual(resultTexture.save(), "test_results/text_drawer/test_3_result.png")
    }

    @After
    fun cleanup() {
        textDrawer.dispose()
    }

    companion object {
        private val FAKE_CANVAS_SIZE = Size(512, 512)
        private val TEST_FONT_1_PATH = "font/people_book_font.otf"
        private val TEST_FONT_2_PATH = "font/OpenSans.ttf"
    }
}
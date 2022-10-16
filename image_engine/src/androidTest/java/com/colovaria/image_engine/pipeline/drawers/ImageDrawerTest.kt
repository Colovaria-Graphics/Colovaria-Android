package com.colovaria.image_engine.pipeline.drawers

import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.colovaria.geometry.RectF
import com.colovaria.geometry.Size
import com.colovaria.graphics.egl.GContext
import com.colovaria.graphics.egl.GDisplay
import com.colovaria.graphics.egl.GSurface
import com.colovaria.graphics.size
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.resources.AssetResourcePath
import com.colovaria.image_engine.api.resources.ImageLoader
import com.colovaria.image_engine.api.resources.ResourcePath
import com.colovaria.image_engine.api.texture.ImageInstruction
import com.colovaria.image_engine.utils.BitmapUtils
import com.colovaria.image_engine.utils.TestClass
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CompletableFuture

@RunWith(AndroidJUnit4::class)
class ImageDrawerTest : TestClass() {
    object FakeImageLoader : ImageLoader {
        override fun load(resource: ResourcePath, targetSize: Size?): CompletableFuture<Bitmap> {
            assert(resource is AssetResourcePath)
            return CompletableFuture.completedFuture(BitmapUtils.loadBitmapFromAssetPath(
                (resource as AssetResourcePath).path
            ))
        }
        override fun size(resource: ResourcePath): CompletableFuture<Size> {
            return load(resource).thenApply { it.size() }
        }
    }

    private val imageDrawer by lazy { ImageDrawer(FakeImageLoader, FAKE_CANVAS_SIZE, true) }

    @Before
    fun setup() {
        val display = GDisplay.create()
        val gpuContext = GContext.create(display)
        val surface = GSurface.create(display, Size(1, 1))
        gpuContext.bind(surface)
    }

    @Test
    fun ImageDrawer_DrawImage_SouldBeAsExpected() {
        val resultTexture = imageDrawer.draw(
            ImageInstruction(AssetResourcePath("test_results/image_drawer/test_image.jpg")), BlenderInstruction()
        )

        BitmapUtils.assertBitmapEqual(resultTexture.save(), "test_results/image_drawer/test_image.jpg")
    }

    @Test
    fun ImageDrawer_DrawImageWithCrop_SouldBeAsExpected() {
        val resultTexture = imageDrawer.draw(
            ImageInstruction(AssetResourcePath("test_results/image_drawer/test_image.jpg"), RectF(0.2f, 0.7f, 0.6f, 0.1f)), BlenderInstruction()
        )

        BitmapUtils.assertBitmapEqual(resultTexture.save(), "test_results/image_drawer/test_1_result.png")
    }

    @After
    fun cleanup() {
        imageDrawer.dispose()
    }

    companion object {
        private val FAKE_CANVAS_SIZE = Size(512, 512)
    }
}
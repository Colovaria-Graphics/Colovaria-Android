package com.colovaria.image_engine.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BitmapsUtilsTest: TestClass() {
    @Test
    fun TestComputeBitmapsDiffPercentage_TwoBlacksBitmaps_DiffShouldBe0() {
        val bitmap1 = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888)
        val bitmap2 = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888)
        val canvas1 = Canvas(bitmap1)
        val canvas2 = Canvas(bitmap2)

        canvas1.drawColor(Color.BLACK)
        canvas2.drawColor(Color.BLACK)

        assertThat(BitmapUtils.computeBitmapsDiffPercentage(bitmap1, bitmap2)).isEqualTo(0f)
    }

    @Test
    fun TestComputeBitmapsDiffPercentage_TwoWhitesBitmaps_DiffShouldBe0() {
        val bitmap1 = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888)
        val bitmap2 = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888)
        val canvas1 = Canvas(bitmap1)
        val canvas2 = Canvas(bitmap2)

        canvas1.drawColor(Color.WHITE)
        canvas2.drawColor(Color.WHITE)

        assertThat(BitmapUtils.computeBitmapsDiffPercentage(bitmap1, bitmap2)).isEqualTo(0f)
    }

    @Test
    fun TestComputeBitmapsDiffPercentage_OnePixelDifferent_DiffShouldBeNot0() {
        val bitmap1 = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888)
        val bitmap2 = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888)
        val canvas1 = Canvas(bitmap1)
        val canvas2 = Canvas(bitmap2)

        canvas1.drawColor(Color.WHITE)
        canvas2.drawColor(Color.WHITE)

        bitmap1.setPixel(0, 0, Color.BLACK)

        assertThat(BitmapUtils.computeBitmapsDiffPercentage(bitmap1, bitmap2))
            .isEqualTo((255 * 3f) / (SIZE * SIZE * 255 * 4f))
    }

    @Test
    fun TestComputeBitmapsDiffPercentage_4PixelDifferent_DiffShouldBeNot0() {
        val bitmap1 = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888)
        val bitmap2 = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888)
        val canvas1 = Canvas(bitmap1)
        val canvas2 = Canvas(bitmap2)

        canvas1.drawColor(Color.WHITE)
        canvas2.drawColor(Color.WHITE)

        bitmap1.setPixel(0, 0, Color.BLACK)
        bitmap1.setPixel(1, 0, Color.BLACK)
        bitmap1.setPixel(2, 0, Color.BLACK)
        bitmap1.setPixel(3, 0, Color.BLACK)

        assertThat(BitmapUtils.computeBitmapsDiffPercentage(bitmap1, bitmap2))
            .isEqualTo((4 * 255 * 3f) / (SIZE * SIZE * 255 * 4f))
    }

    @Test
    fun TestComputeBitmapsDiffPercentage_TransperentAndWhiteBitmaps_DiffShouldBe1() {
        val bitmap1 = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888)
        val bitmap2 = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888)
        val canvas1 = Canvas(bitmap1)
        val canvas2 = Canvas(bitmap2)

        canvas1.drawColor(Color.WHITE)
        canvas2.drawColor(Color.TRANSPARENT)

        assertThat(BitmapUtils.computeBitmapsDiffPercentage(bitmap1, bitmap2)).isEqualTo(1f)
    }

    companion object {
        private const val SIZE = 512
    }
}
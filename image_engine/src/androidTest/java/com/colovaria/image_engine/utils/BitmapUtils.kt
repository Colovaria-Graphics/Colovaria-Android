package com.colovaria.image_engine.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.colovaria.graphics.utils.size
import com.google.common.truth.Truth.assertThat
import java.io.ByteArrayOutputStream
import kotlin.math.abs

object BitmapUtils {
    fun assertBitmapEqual(bitmap: Bitmap, assetPath: String, maxDiff: Float = 0.0099999f) {
        assertThat(compareBitmapToAssert(bitmap, assetPath)).isLessThan(maxDiff)
    }

    /**
     * Like computeBitmapsDiffPercentage, but the second bitmap is asset.
     */
    fun compareBitmapToAssert(bitmap: Bitmap, assetPath: String) : Float {
        return computeBitmapsDiffPercentage(bitmap, loadBitmapFromAssetPath(assetPath))
    }

    /**
     * This function calculate the different in percentages [0..1] between two given bitmaps.
     */
    fun computeBitmapsDiffPercentage(bitmapA: Bitmap, bitmapB: Bitmap) : Float {
        if (bitmapA.size() != bitmapB.size()) {
            // Bitmaps in different size, return 100%.
            return 1f
        }

        val bufferA = IntArray(bitmapA.width * bitmapA.height)
        val bufferB = IntArray(bitmapB.width * bitmapB.height)
        assert(bufferA.size == bufferB.size)

        bitmapA.getPixels(bufferA, 0, bitmapA.width, 0, 0, bitmapA.width, bitmapA.height)
        bitmapB.getPixels(bufferB, 0, bitmapB.width, 0, 0, bitmapB.width, bitmapB.height)

        var totalDiff = 0.0
        val pixelWeight = 1.0 / bufferA.size
        val colorWeight = pixelWeight / (4.0 * 255)
        for (i in bufferA.indices) {
            val pixelA = bufferA[i]
            val pixelB = bufferB[i]

            val diff = abs(((pixelA shr 0) and 0xFF) - ((pixelB shr 0) and 0xFF)) +
                       abs(((pixelA shr 8) and 0xFF) - ((pixelB shr 8) and 0xFF)) +
                       abs(((pixelA shr 16) and 0xFF) - ((pixelB shr 16) and 0xFF)) +
                       abs(((pixelA shr 24) and 0xFF) - ((pixelB shr 24) and 0xFF))

            totalDiff += diff * colorWeight
        }
        
        return totalDiff.toFloat()
    }

    fun encodeBitmap(bitmap: Bitmap) : String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
    }

    fun loadBitmapFromAssetPath(assetPath: String) : Bitmap {
        return TestClass().context.assets.open(assetPath).use(BitmapFactory::decodeStream)
    }
}
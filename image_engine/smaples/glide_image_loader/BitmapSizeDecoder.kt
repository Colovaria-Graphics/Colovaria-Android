package com.glide_image_loader

import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import androidx.exifinterface.media.ExifInterface.*
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import java.io.InputStream

class BitmapSizeDecoder : ResourceDecoder<InputStream, BitmapFactory.Options> {
    override fun handles(inputStream: InputStream, options: Options) : Boolean {
        return true
    }

    override fun decode(inputStream: InputStream, width: Int, height: Int, options: Options) : Resource<BitmapFactory.Options> {
        return SimpleResource(BitmapFactory.Options().apply {
            inJustDecodeBounds = true

            assert(inputStream.markSupported())
            inputStream.mark(0)

            BitmapFactory.decodeStream(inputStream, null, this)?.recycle()

            inputStream.reset()

            // Since we skip the glide code that transpose images, we need to do it manually.
            if (shouldTranspose(inputStream)) {
                val transposedWidth = outHeight
                val transposedHeight = outWidth
                outWidth = transposedWidth
                outHeight = transposedHeight
            }
        })
    }

    private fun shouldTranspose(inputStream: InputStream) : Boolean {
        return when (ExifInterface(inputStream).getAttributeInt(TAG_ORIENTATION, ORIENTATION_NORMAL)) {
            ORIENTATION_ROTATE_90, ORIENTATION_ROTATE_270 -> true
            else -> false
        }
    }
}

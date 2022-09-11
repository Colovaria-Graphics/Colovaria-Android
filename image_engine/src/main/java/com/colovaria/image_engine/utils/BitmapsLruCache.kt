package com.colovaria.image_engine.utils

import android.graphics.Bitmap
import android.util.LruCache
import com.colovaria.geometry.Size

class BitmapsLruCache(maxBitmaps: Int = 10) : LruCache<Size, Bitmap>(maxBitmaps) {
    override fun entryRemoved(evicted: Boolean, key: Size, oldValue: Bitmap, newValue: Bitmap?) {
        oldValue.recycle()
    }

    fun computeIfAbsent(size: Size) : Bitmap {
        return get(size) ?: kotlin.run {
            val bitmap = Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888)
            put(size, bitmap)
            bitmap
        }
    }
}
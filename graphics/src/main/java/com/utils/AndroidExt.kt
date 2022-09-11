package com.utils

import android.graphics.Bitmap
import com.geometry.Rect
import com.geometry.Size

fun Bitmap.size() = Size(width, height)

fun Bitmap.rect() = Rect(0, height, width, 0)
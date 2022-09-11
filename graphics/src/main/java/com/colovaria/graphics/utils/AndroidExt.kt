package com.colovaria.graphics.utils

import android.graphics.Bitmap
import com.colovaria.geometry.Rect
import com.colovaria.geometry.Size

fun Bitmap.size() = Size(width, height)

fun Bitmap.rect() = Rect(0, height, width, 0)
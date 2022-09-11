package com.image_engine.api.text

import android.text.Layout

enum class TextAlignment {
    NORMAL,
    CENTER,
    OPPOSITE;

    fun toAndroidAlignment() = when(this) {
        NORMAL -> Layout.Alignment.ALIGN_NORMAL
        CENTER -> Layout.Alignment.ALIGN_CENTER
        OPPOSITE -> Layout.Alignment.ALIGN_OPPOSITE
    }
}
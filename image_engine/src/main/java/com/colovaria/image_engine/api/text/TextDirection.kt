package com.colovaria.image_engine.api.text

import android.text.TextDirectionHeuristics

enum class TextDirection {
    LTR,
    RTL;

    fun toAndroidDirection() = when(this) {
        LTR -> TextDirectionHeuristics.LTR
        RTL -> TextDirectionHeuristics.RTL
    }
}
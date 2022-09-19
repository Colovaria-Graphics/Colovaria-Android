package com.colovaria.image_engine.api.text

import android.text.TextDirectionHeuristic
import android.text.TextDirectionHeuristics

enum class TextDirection {
    LTR,
    RTL;

    fun toAndroidDirection() : TextDirectionHeuristic = when(this) {
        LTR -> TextDirectionHeuristics.LTR
        RTL -> TextDirectionHeuristics.RTL
    } ?: error("TextDirectionHeuristics is in unknown state")
}
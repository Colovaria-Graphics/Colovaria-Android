package com.colovaria.image_engine

import com.colovaria.geometry.Size
import com.colovaria.image_engine.utils.TestClass

class BlenderTests : TestClass() {
    val blender by lazy { Blender(context, FAKE_CANVAS_SIZE) }



    companion object {
        private val FAKE_CANVAS_SIZE = Size(512, 512)
    }
}
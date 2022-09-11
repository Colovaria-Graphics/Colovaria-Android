package com.colovaria.image_engine.utils

import java.nio.FloatBuffer

object GenericBuffers {
    private val TRIANGLE_STRIP_2D_FULL_ARRAY = floatArrayOf(
        -1.0f, 1.0f,
        -1.0f, -1.0f,
        1.0f, 1.0f,
        1.0f, -1.0f,
    )

    private val TRIANGLE_STRIP_2D_TEXTURE_ARRAY = floatArrayOf(
        0.0f, 1.0f,
        0.0f, 0.0f,
        1.0f, 1.0f,
        1.0f, 0.0f,
    )

    val TRIANGLE_STRIP_2D_FULL: FloatBuffer = FloatBuffer.wrap(TRIANGLE_STRIP_2D_FULL_ARRAY)
    val TRIANGLE_STRIP_2D_FULL_SIZE: Int = TRIANGLE_STRIP_2D_FULL_ARRAY.size

    val TRIANGLE_STRIP_2D_TEXTURE: FloatBuffer = FloatBuffer.wrap(TRIANGLE_STRIP_2D_TEXTURE_ARRAY)
    val TRIANGLE_STRIP_2D_TEXTURE_SIZE: Int = TRIANGLE_STRIP_2D_TEXTURE_ARRAY.size
}
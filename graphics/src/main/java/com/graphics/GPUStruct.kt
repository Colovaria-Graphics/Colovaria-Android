package com.graphics

import java.nio.Buffer

data class GPUStruct(
    val name: String,
    val bufferSize: Int,
    val vertexSize: Int,
    val buffer: Buffer,
) {
    val vertexCount = bufferSize / vertexSize
}
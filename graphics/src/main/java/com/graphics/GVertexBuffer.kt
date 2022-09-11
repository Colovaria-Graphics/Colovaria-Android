package com.graphics

import android.opengl.GLES32
import java.nio.*

class GVertexBuffer : GHandle, GBindableHandle {
    constructor() : super(GLES.glGenBuffer())

    constructor(buffer: Buffer, size: Int) : this() {
        setBuffer(buffer, size)
    }

    override fun bind() : BindReference {
        val lastBounded = boundedHandle()

        if (lastBounded != handle) GLES.glBindBuffer(GLES32.GL_ARRAY_BUFFER, handle)

        return BindReference {
            if (lastBounded != handle) {
                GLES.glBindBuffer(GLES32.GL_ARRAY_BUFFER, lastBounded)
            }
        }
    }

    fun setBuffer(buffer: Buffer, size: Int) {
        val bytesSize = when (buffer) {
            is FloatBuffer -> Float.SIZE_BYTES
            is IntBuffer -> Int.SIZE_BYTES
            is DoubleBuffer -> Double.SIZE_BYTES
            is LongBuffer -> Long.SIZE_BYTES
            is CharBuffer -> Char.SIZE_BYTES
            else -> error("Unknown type: $buffer")
        } * size

        withBind {
            GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, bytesSize, buffer, GLES32.GL_STATIC_DRAW)
        }
    }

    override fun dispose() {
        GLES32.glDeleteBuffers(1, intArrayOf(handle), 0)
        disposed = true
    }

    companion object {
        fun boundedHandle() = GLES.glGetIntegerv(GLES32.GL_ARRAY_BUFFER_BINDING)
    }
}
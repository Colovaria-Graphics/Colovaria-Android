package com.colovaria.graphics.gles

import android.opengl.GLES20
import com.colovaria.graphics.base.BindReference
import com.colovaria.graphics.base.GBindableHandle
import java.nio.*

class GVertexBuffer : GHandle, GBindableHandle {
    constructor() : super(GLES.glGenBuffer())

    constructor(buffer: Buffer, size: Int) : this() {
        setBuffer(buffer, size)
    }

    override fun bind() : BindReference {
        val lastBounded = boundedHandle()

        if (lastBounded != handle) GLES.glBindBuffer(GLES20.GL_ARRAY_BUFFER, handle)

        return BindReference {
            if (lastBounded != handle) {
                GLES.glBindBuffer(GLES20.GL_ARRAY_BUFFER, lastBounded)
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
            GLES.glBufferData(GLES20.GL_ARRAY_BUFFER, bytesSize, buffer, GLES20.GL_STATIC_DRAW)
        }
    }

    override fun dispose() {
        GLES.glDeleteBuffer(handle)
        disposed = true
    }

    companion object {
        fun boundedHandle() = GLES.glGetIntegerv(GLES20.GL_ARRAY_BUFFER_BINDING)
    }
}

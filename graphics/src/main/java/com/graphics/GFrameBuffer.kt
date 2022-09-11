package com.graphics

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLES32
import com.geometry.Size
import java.nio.ByteBuffer

class GFrameBuffer(
    val texture: GTexture
) : GHandle(GLES.glGenFramebuffer()), GBindableHandle {

    constructor(size: Size) : this(GTexture(size))

    init {
        withBind {
            GLES32.glFramebufferTexture2D(GLES32.GL_FRAMEBUFFER, GLES32.GL_COLOR_ATTACHMENT0, GLES32.GL_TEXTURE_2D, texture.handle, 0)
        }
    }

    override fun bind() : BindReference {
        val lastBounded = boundedHandle()

        if (lastBounded != handle) GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER, handle)

        return BindReference {
            if (lastBounded != handle) {
                GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER, lastBounded)
            }
        }
    }

    fun save() : Bitmap {
        val pixelBuffer = ByteBuffer.allocateDirect(texture.size.width * texture.size.height * 4)

        withBind {
            GLES32.glReadPixels(0, 0, texture.size.width, texture.size.height, GLES32.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixelBuffer)
        }

        pixelBuffer.rewind()

        return Bitmap.createBitmap(texture.size.width, texture.size.height, Bitmap.Config.ARGB_8888).apply {
            copyPixelsFromBuffer(pixelBuffer)
        }
    }

    fun clear() = withBind {
        GUtils.clear()
    }

    override fun dispose() {
        GLES32.glDeleteFramebuffers(1, intArrayOf(handle), 0)
        texture.dispose()
        disposed = true
    }

    companion object {
        fun boundedHandle() = GLES.glGetIntegerv(GLES32.GL_FRAMEBUFFER_BINDING)
    }
}
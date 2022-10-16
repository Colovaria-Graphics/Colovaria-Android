package com.colovaria.graphics.gles

import android.graphics.Bitmap
import android.opengl.GLES20
import com.colovaria.geometry.Size
import com.colovaria.graphics.base.BindReference
import com.colovaria.graphics.base.GBindableHandle
import java.nio.ByteBuffer

class GFrameBuffer() : GHandle(GLES.glGenFramebuffer()), GBindableHandle {
    var texture: GTexture? = null
        private set

    constructor(texture: GTexture) : this() {
        attachTexture(texture)
    }

    constructor(size: Size) : this(GTexture(size))

    override fun bind() : BindReference {
        val lastBounded = boundedHandle()

        if (lastBounded != handle) GLES.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, handle)

        return BindReference {
            if (lastBounded != handle) {
                GLES.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, lastBounded)
            }
        }
    }

    /**
     * This function attach a texture to ATTACHMENT0 of the current frame buffer.
     */
    fun attachTexture(newTexture: GTexture) {
        assert(texture == null) { "A texture is already attached" }
        texture = newTexture
        withBind {
            GLES.glFramebufferTexture2D(
                GLES20.GL_FRAMEBUFFER,
                GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D,
                newTexture.handle,
                0
            )
        }
    }

    /**
     * This function detach a texture from the framebuffer, it doesn't do any GLES operation,
     * just a logical object state change.
     * @return the last attached texture, note! the caller should take care and call dispose
     * on this texture.
     */
    fun detachTexture() : GTexture? {
        if (texture == null) return null
        val oldTexture = texture
        texture = null
        return oldTexture
    }

    fun cloneTexture() : GTexture {
        return texture?.clone() ?: error("Can't clone null texture")
    }

    fun clear() {
        withBind { GLESUtils.clear() }
    }

    /**
     * This function dumps the given framebuffer into a bitmap.
     */
    fun save() : Bitmap {
        val texture = texture ?: error("There is no attached texture")

        val pixelBuffer = ByteBuffer.allocateDirect(texture.size.width * texture.size.height * 4)

        withBind {
            GLES.glReadPixels(0, 0, texture.size.width, texture.size.height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixelBuffer)
        }

        pixelBuffer.rewind()

        return Bitmap.createBitmap(texture.size.width, texture.size.height, Bitmap.Config.ARGB_8888).apply {
            copyPixelsFromBuffer(pixelBuffer)
        }
    }

    override fun dispose() {
        GLES.glDeleteFramebuffer(handle)
        texture?.dispose()
        disposed = true
    }

    companion object {
        fun boundedHandle() = GLES.glGetIntegerv(GLES20.GL_FRAMEBUFFER_BINDING)
    }
}

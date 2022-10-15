package com.colovaria.graphics

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLES32
import com.colovaria.geometry.RectF
import com.colovaria.geometry.Size
import com.colovaria.graphics.uniforms.GColor
import com.colovaria.graphics.utils.rect
import com.colovaria.graphics.utils.size
import com.colovaria.graphics.wrappers.GLES

class GTexture : GHandle, GUniform, GBindableHandle {
    val size: Size

    constructor(size: Size) : super(GLES.glGenTexture()) {
        this.size = size
        withBind {
            GLES.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, size.width, size.height, 0, GLES32.GL_RGBA, GLES32.GL_UNSIGNED_BYTE, null)
            commonInit()
        }
    }

    constructor(bitmap: Bitmap, crop: RectF = RectF(0f, 1f, 1f, 0f)) : super(GLES.glGenTexture()) {
        val croppedBitmap = cropBitmap(bitmap, crop)
        this.size = croppedBitmap.size()

        withBind {
            GLES.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, croppedBitmap, 0)
            commonInit()
        }

        if (croppedBitmap !== bitmap) {
            croppedBitmap.recycle()
        }
    }

    override fun bind() : BindReference {
        val lastBounded = boundedHandle()

        if (lastBounded != handle) GLES.glBindTexture(GLES20.GL_TEXTURE_2D, handle)

        return BindReference {
            if (lastBounded != handle) {
                GLES.glBindTexture(GLES20.GL_TEXTURE_2D, lastBounded)
            }
        }
    }

    fun setFiltering(magnifying: Int, minifying: Int) = withBind {
        GLES.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, magnifying)
        GLES.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minifying)
    }

    fun setWrap(method: Int) = withBind {
        GLES.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, method)
        GLES.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, method)
    }

    fun setBorderColor(color: GColor) = withBind {
        GLES.glTexParameterfv(GLES20.GL_TEXTURE_2D,
            GLES32.GL_TEXTURE_BORDER_COLOR,
            color.asArray(),
            0)
    }

    fun clone(): GTexture {
        val frameBuffer = GFrameBuffer()
        val cloneTexture = GTexture(size)
        frameBuffer.attachTexture(this)
        frameBuffer.withBind {
            cloneTexture.withBind {
                GLES.glCopyTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, 0, 0, size, 0)
            }
        }
        frameBuffer.detachTexture()  // Detach current texture so it will not be disposed.
        frameBuffer.dispose()
        return cloneTexture
    }

    fun save() : Bitmap {
        val frameBuffer = GFrameBuffer(this)
        val bitmap = frameBuffer.save()
        frameBuffer.detachTexture() // detach the current texture, so it will not be disposed.
        frameBuffer.dispose()
        return bitmap
    }

    override fun putUniform(attribLocation: Int) {
        GLES.glActiveTexture(GLES20.GL_TEXTURE0 + handle)
        GLES.glUniform1i(attribLocation, handle)
        bind()
    }

    override fun dispose() {
        GLES.glDeleteTexture(handle)
        disposed = true
    }

    private fun commonInit() = withBind {
        setFiltering(GLES20.GL_LINEAR, GLES20.GL_NEAREST)
        // TODO: setWrap(GLES32.GL_CLAMP_TO_BORDER)
        // TODO: setBorderColor(GColor.TRANSPARENT)
    }

    private fun cropBitmap(bitmap: Bitmap, crop: RectF) : Bitmap {
        val bitmapRect = bitmap.rect()
        val cropRect = (crop * bitmapRect.size).clampTo(bitmapRect)

        return if (cropRect != bitmapRect) Bitmap.createBitmap(
            bitmap,
            cropRect.left,
            cropRect.bottom,
            cropRect.width,
            cropRect.height,
        ) else bitmap
    }

    companion object {
        fun boundedHandle() = GLES.glGetIntegerv(GLES20.GL_TEXTURE_BINDING_2D)
    }
}
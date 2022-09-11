package com.colovaria.graphics

import android.graphics.Bitmap
import android.opengl.GLES32
import com.colovaria.geometry.RectF
import com.colovaria.geometry.Size
import com.colovaria.graphics.uniforms.GColor
import com.colovaria.graphics.utils.rect
import com.colovaria.graphics.utils.size

class GTexture : GHandle, GUniform, GBindableHandle {
    val size: Size

    constructor(size: Size) : super(GLES.glGenTexture()) {
        this.size = size
        withBind {
            GLES.glTexImage2D(GLES32.GL_TEXTURE_2D, 0, GLES32.GL_RGBA, size.width, size.height, 0, GLES32.GL_RGBA, GLES32.GL_UNSIGNED_BYTE, null)
            commonInit()
        }
    }

    constructor(bitmap: Bitmap, crop: RectF = RectF(0f, 1f, 1f, 0f)) : super(GLES.glGenTexture()) {
        val croppedBitmap = cropBitmap(bitmap, crop)
        this.size = croppedBitmap.size()

        withBind {
            GLES.glTexImage2D(GLES32.GL_TEXTURE_2D, 0, croppedBitmap, 0)
            commonInit()
        }

        if (croppedBitmap !== bitmap) {
            croppedBitmap.recycle()
        }
    }

    override fun bind() : BindReference {
        val lastBounded = boundedHandle()

        if (lastBounded != handle) GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, handle)

        return BindReference {
            if (lastBounded != handle) {
                GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, lastBounded)
            }
        }
    }

    fun setFiltering(magnifying: Int, minifying: Int) = withBind {
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MAG_FILTER, magnifying)
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MIN_FILTER, minifying)
    }

    fun setWrap(method: Int) = withBind {
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_S, method)
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_T, method)
    }

    fun setBorderColor(color: GColor) = withBind {
        GLES32.glTexParameterfv(GLES32.GL_TEXTURE_2D,
            GLES32.GL_TEXTURE_BORDER_COLOR,
            color.asArray(),
            0)
    }

    fun clone(): GTexture {
        val texture = GTexture(size)
        GLES32.glCopyImageSubData(handle, GLES32.GL_TEXTURE_2D, 0, 0, 0, 0,
            texture.handle, GLES32.GL_TEXTURE_2D, 0, 0, 0, 0, size.width,
            size.height, 1)
        return texture
    }

    fun save() : Bitmap {
        val frameBuffer = GFrameBuffer(clone())
        val bitmap = frameBuffer.save()
        frameBuffer.dispose()
        return bitmap
    }

    override fun putUniform(attribLocation: Int) {
        GLES32.glActiveTexture(GLES32.GL_TEXTURE0 + handle)
        GLES32.glUniform1i(attribLocation, handle)
        bind()
    }

    override fun dispose() {
        GLES32.glDeleteTextures(1, intArrayOf(handle), 0)
        disposed = true
    }

    private fun commonInit() = withBind {
        setFiltering(GLES32.GL_LINEAR, GLES32.GL_NEAREST)
        setWrap(GLES32.GL_CLAMP_TO_BORDER)
        setBorderColor(GColor.TRANSPARENT)
    }

    companion object {
        fun boundedHandle() = GLES.glGetIntegerv(GLES32.GL_TEXTURE_BINDING_2D)

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
    }
}
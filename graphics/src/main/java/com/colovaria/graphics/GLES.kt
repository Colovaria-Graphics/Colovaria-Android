package com.colovaria.graphics

import android.graphics.Bitmap
import android.opengl.GLES32
import android.opengl.GLUtils
import java.nio.Buffer

object GLES {
    fun glGenTexture() : Int = wrapWithErrorCheck {
        val handles = IntArray(1)
        GLES32.glGenTextures(1, handles, 0)
        return@wrapWithErrorCheck handles[0]
    }

    fun glGenFramebuffer() : Int = wrapWithErrorCheck {
        val handles = IntArray(1)
        GLES32.glGenFramebuffers(1, handles, 0)
        return@wrapWithErrorCheck handles[0]
    }

    fun glGenVertexArray() : Int = wrapWithErrorCheck {
        val handles = IntArray(1)
        GLES32.glGenVertexArrays(1, handles, 0)
        return@wrapWithErrorCheck handles[0]
    }

    fun glGenBuffer() : Int = wrapWithErrorCheck {
        val handles = IntArray(1)
        GLES32.glGenBuffers(1, handles, 0)
        return@wrapWithErrorCheck handles[0]
    }

    fun glCreateShader(type: Int) : Int = wrapWithErrorCheck {
        return@wrapWithErrorCheck GLES32.glCreateShader(type)
    }

    fun glCreateProgram() : Int = wrapWithErrorCheck {
        return@wrapWithErrorCheck GLES32.glCreateProgram()
    }

    fun glTexImage2D(
        target: Int,
        level: Int,
        internalFormat: Int,
        width: Int,
        height: Int,
        border: Int,
        format: Int,
        type: Int,
        pixels: Buffer?
    ) = wrapWithErrorCheck {
        GLES32.glTexImage2D(target, level, internalFormat, width, height, border, format, type, pixels)
    }

    fun glTexImage2D(
        target: Int,
        level: Int,
        bitmap: Bitmap,
        border: Int
    ) {
        GLUtils.texImage2D(target, level, bitmap, border)
    }

    fun glGetIntegerv(pname: Int) : Int = wrapWithErrorCheck {
        val array = intArrayOf(0)
        GLES32.glGetIntegerv(pname, array, 0)
        return@wrapWithErrorCheck array[0]
    }

    fun glViewport(x: Int, y: Int, width: Int, height: Int) = wrapWithErrorCheck {
        GLES32.glViewport(x, y, width, height)
    }

    fun glClearColor(red: Float, green: Float, blue: Float, alpha: Float) = wrapWithErrorCheck {
        GLES32.glClearColor(red, green, blue, alpha)
    }

    fun glClear(buffers: Int) = wrapWithErrorCheck {
        GLES32.glClear(buffers)
    }

    fun glBindVertexArray(handle: Int) = wrapWithErrorCheck {
        GLES32.glBindVertexArray(handle)
    }

    fun glDeleteVertexArrays(handles: IntArray, offset: Int) = wrapWithErrorCheck {
        GLES32.glDeleteVertexArrays(handles.size, handles, offset)
    }

    fun glBindBuffer(type: Int, handle: Int) = wrapWithErrorCheck {
        GLES32.glBindBuffer(type, handle)
    }

    private inline fun <R> wrapWithErrorCheck(func: () -> R) : R {
        val result = func()
        if (BuildConfig.DEBUG) {
            GLES32.glGetError().let { assert(it == GLES32.GL_NO_ERROR) { "glError: $it" } }
        }
        return result
    }
}
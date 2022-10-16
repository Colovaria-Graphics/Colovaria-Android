package com.colovaria.graphics.gles

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.GLES32
import android.opengl.GLUtils
import com.colovaria.geometry.Size
import java.nio.Buffer

object GLES {
    fun glGenTexture() : Int = wrapWithErrorCheck {
        val handles = IntArray(1)
        GLES20.glGenTextures(1, handles, 0)
        return@wrapWithErrorCheck handles[0]
    }

    fun glGenFramebuffer() : Int = wrapWithErrorCheck {
        val handles = IntArray(1)
        GLES20.glGenFramebuffers(1, handles, 0)
        return@wrapWithErrorCheck handles[0]
    }

    fun glGenVertexArray() : Int = wrapWithErrorCheck {
        val handles = IntArray(1)
        GLES30.glGenVertexArrays(1, handles, 0)
        return@wrapWithErrorCheck handles[0]
    }

    fun glGenBuffer() : Int = wrapWithErrorCheck {
        val handles = IntArray(1)
        GLES20.glGenBuffers(1, handles, 0)
        return@wrapWithErrorCheck handles[0]
    }

    fun glCreateShader(type: Int) : Int = wrapWithErrorCheck {
        return@wrapWithErrorCheck GLES20.glCreateShader(type)
    }

    fun glCreateProgram() : Int = wrapWithErrorCheck {
        return@wrapWithErrorCheck GLES20.glCreateProgram()
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
        GLES20.glTexImage2D(target, level, internalFormat, width, height, border, format, type, pixels)
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
        GLES20.glGetIntegerv(pname, array, 0)
        return@wrapWithErrorCheck array[0]
    }

    fun glViewport(x: Int, y: Int, width: Int, height: Int) = wrapWithErrorCheck {
        GLES20.glViewport(x, y, width, height)
    }

    fun glClearColor(red: Float, green: Float, blue: Float, alpha: Float) = wrapWithErrorCheck {
        GLES20.glClearColor(red, green, blue, alpha)
    }

    fun glClear(buffers: Int) = wrapWithErrorCheck {
        GLES20.glClear(buffers)
    }

    fun glBindVertexArray(handle: Int) = wrapWithErrorCheck {
        GLES30.glBindVertexArray(handle)
    }

    fun glDeleteVertexArrays(handles: IntArray, offset: Int) = wrapWithErrorCheck {
        GLES30.glDeleteVertexArrays(handles.size, handles, offset)
    }

    fun glBindBuffer(type: Int, handle: Int) = wrapWithErrorCheck {
        GLES20.glBindBuffer(type, handle)
    }

    fun glGetUniformLocation(program: Int, name: String?): Int = wrapWithErrorCheck {
        return@wrapWithErrorCheck GLES20.glGetUniformLocation(program, name)
    }

    fun glGetAttribLocation(program: Int, name: String?) = wrapWithErrorCheck {
        return@wrapWithErrorCheck GLES20.glGetAttribLocation(program, name)
    }

    fun glVertexAttribPointer(
        index: Int,
        size: Int,
        type: Int,
        normalized: Boolean,
        stride: Int,
        offset: Int
    ) = wrapWithErrorCheck {
        GLES20.glVertexAttribPointer(index, size, type, normalized, stride, offset)
    }

    fun glEnableVertexAttribArray(index: Int) = wrapWithErrorCheck {
        GLES20.glEnableVertexAttribArray(index)
    }

    fun glDisableVertexAttribArray(index: Int) = wrapWithErrorCheck {
        GLES20.glDisableVertexAttribArray(index)
    }

    fun glDrawArrays(mode: Int, first: Int, count: Int) = wrapWithErrorCheck {
        GLES20.glDrawArrays(mode, first, count)
    }

    fun glFramebufferTexture2D(
        target: Int,
        attachment: Int,
        textarget: Int,
        texture: Int,
        level: Int
    ) = wrapWithErrorCheck {
        GLES20.glFramebufferTexture2D(target, attachment, textarget, texture, level)
    }

    fun glBindFramebuffer(target: Int, framebuffer: Int) = wrapWithErrorCheck {
        GLES20.glBindFramebuffer(target, framebuffer)
    }

    fun glReadPixels(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        format: Int,
        type: Int,
        pixels: Buffer?
    ) = wrapWithErrorCheck {
        GLES20.glReadPixels(x, y, width, height, format, type, pixels)
    }

    fun glDeleteFramebuffer(handle: Int) = wrapWithErrorCheck {
        GLES20.glDeleteFramebuffers(1, intArrayOf(handle), 0)
    }

    fun glUseProgram(program: Int) = wrapWithErrorCheck {
        GLES20.glUseProgram(program)
    }

    fun glDeleteProgram(program: Int) = wrapWithErrorCheck {
        GLES20.glDeleteProgram(program)
    }

    fun glLinkProgram(program: Int) = wrapWithErrorCheck {
        GLES20.glLinkProgram(program)
    }

    fun glAttachShader(program: Int, shader: Int) = wrapWithErrorCheck {
        GLES20.glAttachShader(program, shader)
    }

    fun glDetachShader(program: Int, shader: Int) = wrapWithErrorCheck {
        GLES20.glDetachShader(program, shader)
    }

    fun glGetProgramiv(program: Int, pname: Int, params: IntArray?, offset: Int) = wrapWithErrorCheck {
        GLES20.glGetProgramiv(program, pname, params, offset)
    }

    fun glGetProgramInfoLog(program: Int) : String = wrapWithErrorCheck {
        return@wrapWithErrorCheck GLES20.glGetProgramInfoLog(program)
    }

    fun glDeleteShader(shader: Int) = wrapWithErrorCheck {
        GLES20.glDeleteShader(shader)
    }

    fun glShaderSource(shader: Int, string: String) = wrapWithErrorCheck {
        GLES20.glShaderSource(shader, string)
    }

    fun glCompileShader(shader: Int) = wrapWithErrorCheck {
        GLES20.glCompileShader(shader)
    }

    fun glGetShaderiv(shader: Int, pname: Int, params: IntArray, offset: Int) = wrapWithErrorCheck {
        GLES20.glGetShaderiv(shader, pname, params, offset)
    }

    fun glGetShaderInfoLog(shader: Int) : String = wrapWithErrorCheck {
        GLES20.glGetShaderInfoLog(shader)
    }

    fun glBindTexture(target: Int, texture: Int) = wrapWithErrorCheck {
        GLES20.glBindTexture(target, texture)
    }

    fun glTexParameteri(target: Int, pname: Int, param: Int) = wrapWithErrorCheck {
        GLES20.glTexParameteri(target, pname, param)
    }

    fun glTexParameterfv(target: Int, pname: Int, params: FloatArray, offset: Int) = wrapWithErrorCheck {
        GLES20.glTexParameterfv(target, pname, params, offset)
    }

    fun glCopyImageSubData(
        srcName: Int,
        srcTarget: Int,
        srcLevel: Int,
        srcX: Int,
        srcY: Int,
        srcZ: Int,
        dstName: Int,
        dstTarget: Int,
        dstLevel: Int,
        dstX: Int,
        dstY: Int,
        dstZ: Int,
        srcWidth: Int,
        srcHeight: Int,
        srcDepth: Int
    ) = wrapWithErrorCheck {
        GLES32.glCopyImageSubData(srcName, srcTarget, srcLevel, srcX, srcY, srcZ, dstName,
            dstTarget, dstLevel, dstX, dstY, dstZ, srcWidth, srcHeight, srcDepth)
    }

    fun glActiveTexture(texture: Int) = wrapWithErrorCheck {
        GLES20.glActiveTexture(texture)
    }

    fun glUniform1i(location: Int, x: Int) = wrapWithErrorCheck {
        GLES20.glUniform1i(location, x)
    }

    fun glUniform1f(location: Int, x: Float) = wrapWithErrorCheck {
        GLES20.glUniform1f(location, x)
    }

    fun glUniform2f(location: Int, x: Float, y: Float) = wrapWithErrorCheck {
        GLES20.glUniform2f(location, x, y)
    }

    fun glUniform3f(location: Int, x: Float, y: Float, z: Float) = wrapWithErrorCheck {
        GLES20.glUniform3f(location, x, y, z)
    }

    fun glUniform4f(location: Int, x: Float, y: Float, z: Float, w: Float) = wrapWithErrorCheck {
        GLES20.glUniform4f(location, x, y, z, w)
    }

    fun glUniform2fv(
        location: Int,
        count: Int,
        value: FloatArray,
        offset: Int
    ) = wrapWithErrorCheck {
        GLES20.glUniform2fv(location, count, value, offset)
    }

    fun glUniform3fv(
        location: Int,
        count: Int,
        value: FloatArray,
        offset: Int
    ) = wrapWithErrorCheck {
        GLES20.glUniform3fv(location, count, value, offset)
    }

    fun glUniformMatrix4fv(
        location: Int,
        count: Int,
        transpose: Boolean,
        value: FloatArray,
        offset: Int
    ) = wrapWithErrorCheck {
        GLES20.glUniformMatrix4fv(location, count, transpose, value, offset)
    }

    fun glDeleteTexture(handle: Int) = wrapWithErrorCheck {
        GLES20.glDeleteTextures(1, intArrayOf(handle), 0)
    }

    fun glBufferData(target: Int, size: Int, buffer: Buffer?, usage: Int) = wrapWithErrorCheck {
        GLES20.glBufferData(target, size, buffer, usage)
    }

    fun glDeleteBuffer(handle: Int) = wrapWithErrorCheck {
        GLES20.glDeleteBuffers(1, intArrayOf(handle), 0)
    }

    fun glCopyTexImage2D(
        target: Int,
        level: Int,
        internalFormat: Int,
        x: Int,
        y: Int,
        size: Size,
        border: Int
    ) = wrapWithErrorCheck {
        GLES20.glCopyTexImage2D(target, level, internalFormat, x, y, size.width, size.height, border)
    }

    private inline fun <R> wrapWithErrorCheck(func: () -> R) : R {
        val result = func()
        val errorCode = GLES20.glGetError()
        if (errorCode != GLES20.GL_NO_ERROR) {
            throw GLESException(errorCode)
        }
        return result
    }
}

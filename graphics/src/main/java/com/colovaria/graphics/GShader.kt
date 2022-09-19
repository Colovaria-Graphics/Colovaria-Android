package com.colovaria.graphics

import android.content.Context
import android.opengl.GLES32

class GShader : GHandle {
    constructor(type: Int) : super(GLES.glCreateShader(type))

    constructor(type: Int, code: String) : this(type) {
        attachCodeToShader(code)
    }

    constructor(context: Context, filename: String) : this(GUtils.filenameToShaderType(filename)) {
        attachCodeToShader(context.assets.open(filename).bufferedReader().use { it.readText() })
    }

    override fun dispose() {
        GLES32.glDeleteShader(handle)
        disposed = true
    }

    private fun attachCodeToShader(code: String) {
        GLES32.glShaderSource(handle, code)
        GLES32.glCompileShader(handle)
        assertShaderCompiled()
    }

    private fun assertShaderCompiled() {
        val isLinked = intArrayOf(GLES32.GL_FALSE)
        GLES32.glGetShaderiv(handle, GLES32.GL_COMPILE_STATUS, isLinked, 0)
        assert(isLinked[0] == GLES32.GL_TRUE) { GLES32.glGetShaderInfoLog(handle) }
    }
}
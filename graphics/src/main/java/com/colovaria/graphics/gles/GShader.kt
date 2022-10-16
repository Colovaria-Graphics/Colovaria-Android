package com.colovaria.graphics.gles

import android.content.Context
import android.opengl.GLES20

class GShader : GHandle {
    constructor(type: Int) : super(GLES.glCreateShader(type))

    constructor(type: Int, code: String) : this(type) {
        attachCodeToShader(code)
    }

    constructor(context: Context, filename: String) : this(GLESUtils.filenameToShaderType(filename)) {
        attachCodeToShader(context.assets.open(filename).bufferedReader().use { it.readText() })
    }

    override fun dispose() {
        GLES.glDeleteShader(handle)
        disposed = true
    }

    private fun attachCodeToShader(code: String) {
        GLES.glShaderSource(handle, code)
        GLES.glCompileShader(handle)
        assertShaderCompiled()
    }

    private fun assertShaderCompiled() {
        val isLinked = intArrayOf(GLES20.GL_FALSE)
        GLES.glGetShaderiv(handle, GLES20.GL_COMPILE_STATUS, isLinked, 0)
        assert(isLinked[0] == GLES20.GL_TRUE) { GLES.glGetShaderInfoLog(handle) }
    }
}

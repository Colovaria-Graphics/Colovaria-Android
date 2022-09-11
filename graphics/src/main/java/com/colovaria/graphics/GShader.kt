package com.colovaria.graphics

import android.content.Context
import android.opengl.GLES32

class GShader : GHandle {
    constructor(type: Int) : super(GLES.glCreateShader(type))

    constructor(type: Int, code: String) : this(type) {
        attachCodeToShader(code)
    }

    constructor(context: Context, filename: String) : this(filenameToShaderType(filename)) {
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

    companion object {
        private fun filenameToShaderType(filename: String) = when (filename.split(".").last()) {
            "frag" -> GLES32.GL_FRAGMENT_SHADER
            "vert" -> GLES32.GL_VERTEX_SHADER
            "tesc" -> GLES32.GL_TESS_CONTROL_SHADER
            "tese" -> GLES32.GL_TESS_EVALUATION_SHADER
            "geom" -> GLES32.GL_GEOMETRY_SHADER
            "comp" -> GLES32.GL_COMPUTE_SHADER
            else -> error("unknown file type: $filename")
        }
    }
}
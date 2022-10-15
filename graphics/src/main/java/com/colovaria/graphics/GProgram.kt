package com.colovaria.graphics

import android.opengl.GLES20
import com.colovaria.graphics.wrappers.GLES

class GProgram : GHandle {
    constructor() : super(GLES.glCreateProgram())

    constructor(shaders: List<GShader>) : this() {
        attachShadersToProgram(shaders)
    }

    fun bind() {
        GLES.glUseProgram(handle)
    }

    override fun dispose() {
        GLES.glDeleteProgram(handle)
        disposed = true
    }

    private fun attachShadersToProgram(shaders: List<GShader>) {
        shaders.forEach { GLES.glAttachShader(handle, it.handle) }
        GLES.glLinkProgram(handle)
        shaders.forEach { GLES.glDetachShader(handle, it.handle) }
        assertProgramLinked()
    }

    private fun assertProgramLinked() {
        val isLinked = intArrayOf(GLES20.GL_FALSE)
        GLES.glGetProgramiv(handle, GLES20.GL_LINK_STATUS, isLinked, 0)
        assert(isLinked[0] == GLES20.GL_TRUE) { GLES.glGetProgramInfoLog(handle) }
    }
}
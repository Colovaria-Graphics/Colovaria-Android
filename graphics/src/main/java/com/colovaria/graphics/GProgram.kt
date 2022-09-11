package com.colovaria.graphics

import android.opengl.GLES32

class GProgram : GHandle {
    constructor() : super(GLES32.glCreateProgram())

    constructor(shaders: List<GShader>) : this() {
        attachShadersToProgram(shaders)
    }

    fun bind() {
        GLES32.glUseProgram(handle)
    }

    override fun dispose() {
        GLES32.glDeleteProgram(handle)
        disposed = true
    }

    private fun attachShadersToProgram(shaders: List<GShader>) {
        shaders.forEach { GLES32.glAttachShader(handle, it.handle) }
        GLES32.glLinkProgram(handle)
        shaders.forEach { GLES32.glDetachShader(handle, it.handle) }
        assertProgramLinked()
    }

    private fun assertProgramLinked() {
        val isLinked = intArrayOf(GLES32.GL_FALSE)
        GLES32.glGetProgramiv(handle, GLES32.GL_LINK_STATUS, isLinked, 0)
        assert(isLinked[0] == GLES32.GL_TRUE) { GLES32.glGetProgramInfoLog(handle) }
    }
}
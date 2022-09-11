package com.graphics

import android.opengl.GLES32

class GDynamicDrawer private constructor(
    private val program: GProgram,
    gpuStructs: Array<out GPUStruct>
) : GObject() {
    private val vertexArray = GVertexArray()

    private val vertexBufferEntries: List<VertexBufferEntry> = vertexArray.withBind {
        gpuStructs.map {
            VertexBufferEntry(it.name, it.vertexSize, GVertexBuffer(it.buffer, it.bufferSize))
        }
    }
    private val vertexCount = gpuStructs.firstOrNull()?.vertexCount ?: -1

    private val enabledVertexAttrib = mutableListOf<Int>()

    init {
        assert(gpuStructs.all { it.vertexCount == vertexCount })
    }

    fun draw(mode: Int, uniforms: Map<String, GUniform>, vertices: Map<String, GVertex>) {
        val vaoBindReference = vertexArray.bind()
        program.bind()

        uniforms.forEach { (name, uniform) ->
            val attribLocation = GLES32.glGetUniformLocation(program.handle, name)
            assert(attribLocation >= 0)
            uniform.putUniform(attribLocation)
        }

        vertices.forEach { (name, vertex) ->
            val attribLocation = GLES32.glGetAttribLocation(program.handle, name)
            assert(attribLocation >= 0)
            vertex.putVertex(attribLocation)

            enabledVertexAttrib.add(attribLocation)
        }

        vertexBufferEntries.forEach {
            val attribLocation = GLES32.glGetAttribLocation(program.handle, it.name)
            assert(attribLocation >= 0)

            it.vertexBuffer.withBind {
                GLES32.glVertexAttribPointer(attribLocation, it.vertexSize, GLES32.GL_FLOAT, false, 0, 0)
            }

            enabledVertexAttrib.add(attribLocation)
        }

        enabledVertexAttrib.forEach {  GLES32.glEnableVertexAttribArray(it) }

        vertexArray.withBind {
            GLES32.glDrawArrays(mode, 0, vertexCount)
        }

        enabledVertexAttrib.forEach {  GLES32.glDisableVertexAttribArray(it) }
        enabledVertexAttrib.clear()
        vaoBindReference.unbind()
    }

    override fun dispose() {
        vertexBufferEntries.forEach { it.vertexBuffer.dispose() }
        vertexArray.dispose()
        disposed = true
    }

    private data class VertexBufferEntry(
        val name: String,
        val vertexSize: Int,
        val vertexBuffer: GVertexBuffer,
    )

    object Factory {
        fun create(program: GProgram, vararg gpuStructs: GPUStruct) : GDynamicDrawer {
            return GDynamicDrawer(program, gpuStructs)
        }

        fun create(software: GSoftware, vararg gpuStructs: GPUStruct) : GDynamicDrawer {
            return GDynamicDrawer(software.program, gpuStructs)
        }
    }
}
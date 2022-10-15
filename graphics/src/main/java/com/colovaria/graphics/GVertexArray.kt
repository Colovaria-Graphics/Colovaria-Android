package com.colovaria.graphics

import com.colovaria.graphics.wrappers.GLES

class GVertexArray : GHandle(GLES.glGenVertexArray()), GBindableHandle {
    override fun bind() : BindReference {
        GLES.glBindVertexArray(handle)
        return BindReference {
            // TODO: Duo to OpenGL bug, we can't query the last bounded VAO.
        }
    }

    override fun dispose() {
        GLES.glDeleteVertexArrays(intArrayOf(handle), 0)
        disposed = true
    }
}
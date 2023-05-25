package com.colovaria.graphics.gles

import com.colovaria.graphics.base.BindReference
import com.colovaria.graphics.base.GBindableHandle

class GVertexArray : GHandle(GLES.glGenVertexArray()), GBindableHandle {
    override fun bind() : BindReference {
        GLES.glBindVertexArray(handle)
        return BindReference {
            // TODO: Duo to OpenGL bug, we can't query the last bounded VAO.
        }
    }

    override fun actualDispose() {
        GLES.glDeleteVertexArrays(intArrayOf(handle), 0)
    }
}

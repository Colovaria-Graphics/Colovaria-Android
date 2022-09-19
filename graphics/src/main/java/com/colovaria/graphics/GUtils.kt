package com.colovaria.graphics

import android.opengl.GLES32
import com.colovaria.geometry.Size
import com.colovaria.graphics.uniforms.GColor

object GUtils {
    fun clear(color: GColor = GColor.TRANSPARENT) {
        GLES.glClearColor(color.red, color.green, color.blue, color.alpha)
        GLES.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)
    }

    fun setViewportSize(size: Size) {
        GLES.glViewport(0, 0, size.width, size.height)
    }

    fun filenameToShaderType(filename: String) = when (filename.split(".").last()) {
        "frag" -> GLES32.GL_FRAGMENT_SHADER
        "vert" -> GLES32.GL_VERTEX_SHADER
        "tesc" -> GLES32.GL_TESS_CONTROL_SHADER
        "tese" -> GLES32.GL_TESS_EVALUATION_SHADER
        "geom" -> GLES32.GL_GEOMETRY_SHADER
        "comp" -> GLES32.GL_COMPUTE_SHADER
        else -> error("unknown file type: $filename")
    }
}
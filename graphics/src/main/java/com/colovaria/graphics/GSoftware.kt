package com.colovaria.graphics

import android.content.Context

class GSoftware : GObject {
    val shaders: List<GShader>
    val program: GProgram

    constructor(shaders: List<GShader>) : super() {
        this.shaders = shaders
        this.program = GProgram(shaders)
    }

    constructor(context: Context, vararg shadersAssetPaths: String) : super() {
        this.shaders = shadersAssetPaths.map { GShader(context, it) }
        this.program = GProgram(shaders)
    }

    fun bind() {
        program.bind()
    }

    override fun dispose() {
        shaders.forEach { it.dispose() }
        program.dispose()
        disposed = true
    }
}
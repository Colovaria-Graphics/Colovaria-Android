package com.image_engine.pipeline

abstract class PipelineNode {
    abstract fun preFrameDraw()

    abstract fun postFrameDraw()

    abstract fun dispose()
}
package com.colovaria.image_engine.pipeline.base

interface PipelineNode {
    fun preFrameDraw()
    fun postFrameDraw()
    fun dispose()
}
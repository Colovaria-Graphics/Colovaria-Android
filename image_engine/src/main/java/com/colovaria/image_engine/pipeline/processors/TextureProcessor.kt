package com.colovaria.image_engine.pipeline.processors

import androidx.annotation.CallSuper
import com.colovaria.graphics.GTexture
import com.colovaria.image_engine.api.texture.ProcessorInstruction
import com.colovaria.image_engine.pipeline.PipelineNode

abstract class TextureProcessor<T: ProcessorInstruction>(
    private val manageAllocatedTextures: Boolean = true
) : PipelineNode() {
    private val allocatedTextures = mutableListOf<GTexture>()

    @CallSuper
    override fun preFrameDraw() {
        if (manageAllocatedTextures) assert(allocatedTextures.isEmpty())
    }

    fun process(texture: GTexture, instruction: T) : GTexture {
        val result = processInternal(texture, instruction)
        if (manageAllocatedTextures) allocatedTextures.add(result)
        return result
    }

    @CallSuper
    override fun postFrameDraw() {
        // TODO: maybe it's a better idea to release allocated textures after each layer pass not the whole frame.
        if (manageAllocatedTextures) {
            allocatedTextures.forEach { it.dispose() }
            allocatedTextures.clear()
        }
    }

    @CallSuper
    override fun dispose() {
        postFrameDraw()
    }

    abstract fun processInternal(texture: GTexture, instruction: T) : GTexture
}
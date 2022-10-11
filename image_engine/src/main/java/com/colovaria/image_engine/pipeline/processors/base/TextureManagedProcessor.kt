package com.colovaria.image_engine.pipeline.processors.base

import androidx.annotation.CallSuper
import com.colovaria.graphics.GTexture
import com.colovaria.image_engine.api.texture.base.ProcessorInstruction

abstract class TextureManagedProcessor<InstructionType : ProcessorInstruction> :
    TextureProcessor<InstructionType> {
    private val allocatedTextures = mutableListOf<GTexture>()

    @CallSuper
    override fun preFrameDraw() {
        assert(allocatedTextures.isEmpty())
    }

    override fun process(instruction: InstructionType, texture: GTexture) : GTexture {
        return processInternal(instruction, texture).also(allocatedTextures::add)
    }

    @CallSuper
    override fun postFrameDraw() {
        // TODO: maybe it's a better idea to release allocated textures after each layer pass not the whole frame.
        allocatedTextures.forEach { it.dispose() }
        allocatedTextures.clear()
    }

    @CallSuper
    override fun dispose() {
        postFrameDraw()
    }

    abstract fun processInternal(instruction: InstructionType, texture: GTexture) : GTexture
}
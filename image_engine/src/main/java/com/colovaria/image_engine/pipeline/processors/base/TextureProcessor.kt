package com.colovaria.image_engine.pipeline.processors.base

import com.colovaria.graphics.GTexture
import com.colovaria.image_engine.api.texture.base.ProcessorInstruction
import com.colovaria.image_engine.pipeline.base.PipelineNode

interface TextureProcessor<InstructionType : ProcessorInstruction> : PipelineNode {
    fun process(instruction: InstructionType, texture: GTexture): GTexture
}

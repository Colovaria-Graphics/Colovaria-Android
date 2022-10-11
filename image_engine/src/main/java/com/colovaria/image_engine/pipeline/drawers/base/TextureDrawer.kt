package com.colovaria.image_engine.pipeline.drawers.base

import com.colovaria.graphics.GTexture
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.texture.base.DrawerInstruction
import com.colovaria.image_engine.pipeline.base.PipelineNode

interface TextureDrawer<InstructionType : DrawerInstruction> : PipelineNode {
    fun draw(instruction: InstructionType, blending: BlenderInstruction): GTexture
}
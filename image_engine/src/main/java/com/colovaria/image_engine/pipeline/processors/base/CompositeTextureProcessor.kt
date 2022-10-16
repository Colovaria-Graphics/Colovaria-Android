package com.colovaria.image_engine.pipeline.processors.base

import com.colovaria.geometry.Size
import com.colovaria.graphics.gles.GTexture
import com.colovaria.image_engine.api.texture.base.ProcessorInstruction
import kotlin.reflect.KClass

class CompositeTextureProcessor(val size: Size) : TextureManagedProcessor<ProcessorInstruction>() {
    private val processors = mutableMapOf<KClass<out ProcessorInstruction>, TextureProcessor<in ProcessorInstruction>>()

    fun <I: ProcessorInstruction, D : TextureProcessor<I>> attachProcessor(instructionClass: KClass<I>, textureProcessor: D) {
        require(!processors.containsKey(instructionClass)) { "Processor already exists for $instructionClass" }
        @Suppress("UNCHECKED_CAST")
        processors[instructionClass] = textureProcessor as TextureProcessor<in ProcessorInstruction>
    }

    inline fun <reified I : ProcessorInstruction, D : TextureProcessor<I>> attachProcessor(textureProcessor: D) {
        attachProcessor(I::class, textureProcessor)
    }

    fun detachProcessor(instructionClass: KClass<ProcessorInstruction>) {
        require(processors.containsKey(instructionClass))
        processors.remove(instructionClass)
    }

    fun hasProcessor(instructionClass: KClass<ProcessorInstruction>) : Boolean {
        return processors.containsKey(instructionClass)
    }

    override fun preFrameDraw() {
        super.preFrameDraw()
        processors.values.forEach { it.preFrameDraw() }
    }

    override fun processInternal(instruction: ProcessorInstruction, texture: GTexture): GTexture {
        return processors[instruction::class]?.process(instruction, texture)
            ?: error("No matching drawer for $instruction")
    }

    override fun postFrameDraw() {
        super.postFrameDraw()
        processors.values.forEach { it.postFrameDraw() }
    }

    override fun dispose() {
        super.dispose()
        processors.values.forEach { it.dispose() }
    }
}
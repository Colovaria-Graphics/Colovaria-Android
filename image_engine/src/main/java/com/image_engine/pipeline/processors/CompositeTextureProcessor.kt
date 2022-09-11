package com.image_engine.pipeline.processors

import android.content.Context
import com.geometry.Size
import com.graphics.GTexture
import com.image_engine.api.texture.HSVInstruction
import com.image_engine.api.texture.ProcessorInstruction

class CompositeTextureProcessor(
    context: Context,
    val size: Size
) : TextureProcessor<ProcessorInstruction>(false) {
    private val hsvProcessor = HSVProcessor(context, size)

    private val processors = listOf(hsvProcessor)

    override fun preFrameDraw() {
        super.preFrameDraw()
        processors.forEach { it.preFrameDraw() }
    }

    override fun processInternal(texture: GTexture, instruction: ProcessorInstruction) : GTexture {
        return when (instruction) {
            is HSVInstruction -> hsvProcessor.process(texture, instruction)
        }
    }

    override fun postFrameDraw() {
        super.postFrameDraw()
        processors.forEach { it.postFrameDraw() }
    }

    override fun dispose() {
        super.dispose()
        processors.forEach { it.dispose() }
    }
}
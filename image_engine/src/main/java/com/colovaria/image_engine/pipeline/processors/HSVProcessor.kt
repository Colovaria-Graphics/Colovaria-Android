package com.colovaria.image_engine.pipeline.processors

import android.content.Context
import android.opengl.GLES32
import com.colovaria.geometry.Size
import com.colovaria.graphics.*
import com.colovaria.graphics.uniforms.GFloat
import com.colovaria.image_engine.api.texture.HSVInstruction
import com.colovaria.image_engine.pipeline.processors.base.TextureManagedProcessor
import com.colovaria.image_engine.utils.GenericBuffers

class HSVProcessor(
    context: Context,
    size: Size,
) : TextureManagedProcessor<HSVInstruction>() {
    private val hsvSoftware = GSoftware(context, "shaders/hsv.vert", "shaders/hsv.frag")

    // TODO: why not to use single struct and convert to the other in the vertex shader.
    private val hsvDrawer = GDynamicDrawer.create(hsvSoftware,
        GPUStruct("position", GenericBuffers.TRIANGLE_STRIP_2D_FULL_SIZE,
            2, GenericBuffers.TRIANGLE_STRIP_2D_FULL),
        GPUStruct("texturePosition", GenericBuffers.TRIANGLE_STRIP_2D_TEXTURE_SIZE,
            2, GenericBuffers.TRIANGLE_STRIP_2D_TEXTURE)
    )

    private val fbo = GFrameBuffer(size)

    override fun processInternal(instruction: HSVInstruction, texture: GTexture): GTexture {
        fbo.withBind {
            hsvDrawer.draw(
                GLES32.GL_TRIANGLE_STRIP, mapOf(
                    "backTexture" to texture,
                    "hueFactor" to GFloat(instruction.hueFactor),
                    "saturationFactor" to GFloat(instruction.saturationFactor),
                    "valueFactor" to GFloat(instruction.valueFactor),
                ), mapOf())
        }

        return fbo.cloneTexture()
    }

    override fun dispose() {
        super.dispose()
        hsvDrawer.dispose()
        hsvSoftware.dispose()
        fbo.dispose()
    }
}
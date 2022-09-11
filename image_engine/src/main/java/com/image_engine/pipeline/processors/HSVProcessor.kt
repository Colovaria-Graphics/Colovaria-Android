package com.image_engine.pipeline.processors

import android.content.Context
import android.opengl.GLES32
import com.geometry.Size
import com.graphics.*
import com.graphics.uniforms.GFloat
import com.image_engine.api.texture.HSVInstruction
import com.image_engine.utils.GenericBuffers

class HSVProcessor(
    context: Context,
    val size: Size,
) : TextureProcessor<HSVInstruction>() {
    private val hsvSoftware = GSoftware(context, "shaders/hsv.vert", "shaders/hsv.frag")

    private val hsvDrawer = GDynamicDrawer.Factory.create(hsvSoftware,
        GPUStruct("position", GenericBuffers.TRIANGLE_STRIP_2D_FULL_SIZE,
            2, GenericBuffers.TRIANGLE_STRIP_2D_FULL),
        GPUStruct("texturePosition", GenericBuffers.TRIANGLE_STRIP_2D_TEXTURE_SIZE,
            2, GenericBuffers.TRIANGLE_STRIP_2D_TEXTURE)
    )

    private val fbo = GFrameBuffer(size)

    override fun processInternal(texture: GTexture, instruction: HSVInstruction): GTexture {
        fbo.withBind {
            hsvDrawer.draw(
                GLES32.GL_TRIANGLE_STRIP, mapOf(
                    "backTexture" to texture,
                    "hueFactor" to GFloat(instruction.hueFactor),
                    "saturationFactor" to GFloat(instruction.saturationFactor),
                    "valueFactor" to GFloat(instruction.valueFactor),
                ), mapOf())
        }

        return fbo.texture.clone()
    }

    override fun dispose() {
        super.dispose()
        hsvDrawer.dispose()
        hsvSoftware.dispose()
        fbo.dispose()
    }
}
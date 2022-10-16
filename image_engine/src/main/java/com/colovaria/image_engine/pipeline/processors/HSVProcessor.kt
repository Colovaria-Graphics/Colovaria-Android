package com.colovaria.image_engine.pipeline.processors

import android.content.Context
import android.opengl.GLES32
import com.colovaria.geometry.Size
import com.colovaria.graphics.gles.*
import com.colovaria.graphics.gles.uniforms.GFloat
import com.colovaria.image_engine.api.texture.HSVInstruction
import com.colovaria.image_engine.pipeline.processors.base.TextureManagedProcessor
import com.colovaria.image_engine.utils.GenericBuffers

class HSVProcessor(
    context: Context,
    size: Size,
) : TextureManagedProcessor<HSVInstruction>() {
    private val hsvSoftware = GSoftware(context, "shaders/hsv.vert", "shaders/hsv.frag")

    private val hsvDrawer = GDynamicDrawer.create(hsvSoftware,
        GPUStruct("position", GenericBuffers.TRIANGLE_STRIP_2D_FULL_SIZE,
            2, GenericBuffers.TRIANGLE_STRIP_2D_FULL)
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
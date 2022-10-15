package com.colovaria.image_engine.pipeline.drawers

import android.content.Context
import android.opengl.GLES32
import com.colovaria.geometry.Size
import com.colovaria.graphics.*
import com.colovaria.graphics.uniforms.GInt
import com.colovaria.graphics.uniforms.GVec2FArray
import com.colovaria.graphics.uniforms.GVec3FArray
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.texture.Gradient2DInstruction
import com.colovaria.image_engine.pipeline.drawers.base.CachedTextureDrawer
import com.colovaria.image_engine.utils.GenericBuffers

class Gradient2DDrawer(
    context: Context,
    val size: Size
) : CachedTextureDrawer<Gradient2DInstruction>() {
    private val gradientSoftware =
        GSoftware(context, "shaders/gradient2d.vert", "shaders/gradient2d.frag")

    // TODO: why not to use single struct and convert to the other in the vertex shader.
    private val gradientDrawer = GDynamicDrawer.create(gradientSoftware,
        GPUStruct("position", GenericBuffers.TRIANGLE_STRIP_2D_FULL_SIZE,
            2, GenericBuffers.TRIANGLE_STRIP_2D_FULL),
        GPUStruct("texturePosition", GenericBuffers.TRIANGLE_STRIP_2D_TEXTURE_SIZE,
            2, GenericBuffers.TRIANGLE_STRIP_2D_TEXTURE)
    )

    private val fbo = GFrameBuffer(size)

    override fun drawInternal(instruction: Gradient2DInstruction, blending: BlenderInstruction): GTexture {
        assert(instruction.colors.size == instruction.colors.size)
        assert(instruction.colors.size <= MAX_ACTIVE_DOTS)

        fbo.withBind {
            gradientDrawer.draw(GLES32.GL_TRIANGLE_STRIP, mapOf(
                "colors" to GVec3FArray(instruction.colors.map { it.value.asVec3() }),
                "positions" to GVec2FArray(instruction.colors.map { it.key }),
                "activeDots" to GInt(instruction.colors.size),
            ), mapOf())
        }

        return fbo.cloneTexture()

    }

    override fun dispose() {
        super.dispose()
        fbo.dispose()
        gradientDrawer.dispose()
        gradientSoftware.dispose()
    }

    companion object {
        private const val MAX_ACTIVE_DOTS = 6
    }
}
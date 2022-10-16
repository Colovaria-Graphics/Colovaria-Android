package com.colovaria.image_engine.pipeline.drawers

import android.content.Context
import android.opengl.GLES32
import com.colovaria.geometry.Size
import com.colovaria.graphics.gles.*
import com.colovaria.graphics.gles.uniforms.GInt
import com.colovaria.graphics.gles.uniforms.GVector2F
import com.colovaria.graphics.gles.uniforms.GVector3F
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.texture.Gradient2DInstruction
import com.colovaria.image_engine.pipeline.drawers.base.CachedTextureDrawer
import com.colovaria.image_engine.utils.GenericBuffers
import java.lang.Integer.min

class Gradient2DDrawer(
    context: Context,
    val size: Size
) : CachedTextureDrawer<Gradient2DInstruction>() {
    private val gradientSoftware =
        GSoftware(context, "shaders/gradient2d.vert", "shaders/gradient2d.frag")

    private val gradientDrawer = GDynamicDrawer.create(gradientSoftware,
        GPUStruct("position", GenericBuffers.TRIANGLE_STRIP_2D_FULL_SIZE,
            2, GenericBuffers.TRIANGLE_STRIP_2D_FULL)
    )

    private val fbo = GFrameBuffer(size)

    override fun drawInternal(instruction: Gradient2DInstruction, blending: BlenderInstruction): GTexture {
        assert(instruction.colors.isNotEmpty() && instruction.colors.size <= MAX_ACTIVE_DOTS)

        val uniforms = mutableMapOf<String, GUniform>()
        val entries = instruction.colors.entries.toList()
        for (i in 0 until MAX_ACTIVE_DOTS) {
            val colorIndex = min(instruction.colors.size - 1, i)
            uniforms["color$i"] = GVector3F(entries[colorIndex].value.asVec3())
            uniforms["position$i"] = GVector2F(entries[colorIndex].key)
        }

        uniforms["activePoints"] = GInt(instruction.colors.size)

        fbo.withBind {
            gradientDrawer.draw(GLES32.GL_TRIANGLE_STRIP, uniforms, mapOf())
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
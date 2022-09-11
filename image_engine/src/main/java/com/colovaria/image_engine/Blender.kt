package com.colovaria.image_engine

import android.content.Context
import android.opengl.GLES32
import com.colovaria.geometry.Matrix4F
import com.colovaria.geometry.Size
import com.colovaria.graphics.GDynamicDrawer
import com.colovaria.graphics.GPUStruct
import com.colovaria.graphics.GSoftware
import com.colovaria.graphics.GTexture
import com.colovaria.graphics.uniforms.GBool
import com.colovaria.graphics.uniforms.GFloat
import com.colovaria.graphics.uniforms.GMat4
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.mask.MaskType
import com.colovaria.image_engine.utils.GenericBuffers

class Blender(context: Context, val size: Size) {
    private val nullTexture = GTexture(Size(1, 1))

    private val blenderSoftware = GSoftware(context, "shaders/blender.vert", "shaders/blander.frag")

    private val blenderDrawer = GDynamicDrawer.Factory.create(blenderSoftware,
        GPUStruct("position", GenericBuffers.TRIANGLE_STRIP_2D_FULL_SIZE,
            2, GenericBuffers.TRIANGLE_STRIP_2D_FULL),
        GPUStruct("texturePosition", GenericBuffers.TRIANGLE_STRIP_2D_TEXTURE_SIZE,
            2, GenericBuffers.TRIANGLE_STRIP_2D_TEXTURE)
    )

    fun blend(
        backTexture: GTexture,
        frontTexture: GTexture,
        blending: BlenderInstruction,
        maskTexture: GTexture? = null,
        maskBlending: BlenderInstruction? = null,
        maskType: MaskType? = null,
        flipX: Boolean = false,
    ) {
        val model = Matrix4F.identity().let {
            if (flipX) it.scale(1f, -1f, 1f) else it
        }

        blenderDrawer.draw(GLES32.GL_TRIANGLE_STRIP, mapOf(
            "model" to GMat4(model),
            "blendMode" to blending.blendMode,
            "backTexture" to backTexture,
            "opacity" to GFloat(blending.opacity),
            "frontTexture" to frontTexture,
            "frontModel" to computeModel(blending, frontTexture),
            "maskEnabled" to GBool(maskTexture != null),
            "maskOpacity" to GFloat(maskBlending?.opacity ?: 0f),
            "maskTexture" to (maskTexture ?: nullTexture),
            "maskModel" to computeModel(maskBlending ?: BlenderInstruction(), maskTexture ?: nullTexture),
            "maskType" to (maskType ?: MaskType.ALPHA),
        ), mapOf())
    }

    fun dispose() {
        nullTexture.dispose()
        blenderDrawer.dispose()
        blenderSoftware.dispose()
    }

    private fun computeModel(blending: BlenderInstruction, texture: GTexture) : GMat4 {
        val ratioScale = blending.adjustMode.targetSize(texture.size, size) / size

        val scaleX = 1f / (ratioScale.width * blending.scale.x)
        val scaleY = 1f / (ratioScale.height * blending.scale.y)

        return GMat4(
            Matrix4F
                .identity()
                .translate(-0.5f, -0.5f, 0f)
                .scale(scaleX, scaleY, 1f)
                .translate(
                    0.5f + (blending.center.x - 0.5f) * scaleX,
                    0.5f + (blending.center.y - 0.5f) * scaleY,
                    0f
                )

        )
    }
}
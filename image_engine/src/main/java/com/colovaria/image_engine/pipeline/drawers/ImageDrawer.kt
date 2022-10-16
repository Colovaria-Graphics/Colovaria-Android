package com.colovaria.image_engine.pipeline.drawers

import com.colovaria.geometry.Size
import com.colovaria.geometry.SizeF
import com.colovaria.graphics.gles.GTexture
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.resources.ImageLoader
import com.colovaria.image_engine.api.texture.ImageInstruction
import com.colovaria.image_engine.pipeline.drawers.base.CachedTextureDrawer

class ImageDrawer(
    private val imageLoader: ImageLoader,
    private val size: Size,
    private val optimizeImageSize: Boolean,
) : CachedTextureDrawer<ImageInstruction>() {
    private val fullCropSize = SizeF(1f, 1f)

    override fun drawInternal(instruction: ImageInstruction, blending: BlenderInstruction): GTexture {
        val bitmap = imageLoader.load(
            instruction.path,
            targetSize = if (optimizeImageSize) approxTargetSize(instruction, blending) else null
        ).get()

        return GTexture(bitmap, instruction.crop)
    }

    /**
     * In order not to load a very big bitmaps when rendering to small target,
     * We approximate the size of the bitmap in the target in order to load optimized size.
     */
    private fun approxTargetSize(instruction: ImageInstruction, blending: BlenderInstruction) : Size {
        val imageSize = imageLoader.size(instruction.path).get()
        val cropScale = fullCropSize / instruction.crop.size
        return (blending.adjustMode.targetSize(imageSize, size) * cropScale * blending.scale).roundToSize()
    }
}
package com.colovaria.image_engine

import android.content.Context
import com.colovaria.geometry.Size
import com.colovaria.graphics.GFrameBuffer
import com.colovaria.graphics.GTexture
import com.colovaria.graphics.GUtils
import com.colovaria.image_engine.api.Frame
import com.colovaria.image_engine.api.Layer
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.resources.ImageLoader
import com.colovaria.image_engine.api.texture.GroupInstruction
import com.colovaria.image_engine.api.texture.ProcessorInstruction
import com.colovaria.image_engine.pipeline.drawers.CompositeTextureDrawer
import com.colovaria.image_engine.pipeline.drawers.TextureDrawer
import com.colovaria.image_engine.pipeline.processors.CompositeTextureProcessor
import com.colovaria.image_engine.text.FontProvider
import com.colovaria.image_engine.text.TextMeasurer
import com.colovaria.image_engine.utils.ObjectPool

class FrameCompositor(
    context: Context,
    private val size: Size,
    imageLoaderProvider: () -> ImageLoader,
    optimizeImageSize: Boolean = false
) {
    private val frameBufferPool = ObjectPool(
        { GFrameBuffer(size) },
        GFrameBuffer::clear,
        GFrameBuffer::dispose,
        initialAmount = PASS_FBO_NUM
    )

    private val fontProvider = FontProvider(context)
    private val imageLoader = imageLoaderProvider()
    private val textMeasurer = TextMeasurer(fontProvider)

    private val blender = Blender(context, size)
    private val textureDrawers = CompositeTextureDrawer(
        context, imageLoader, textMeasurer, fontProvider, size, optimizeImageSize
    )
    private val textureProcessors = CompositeTextureProcessor(context, size)
    private val groupDrawer = GroupDrawer()

    init {
        GUtils.setViewportSize(size)
    }

    fun render(frame: Frame, renderToSurface: Boolean = true) {
        textureDrawers.preFrameDraw()
        textureProcessors.preFrameDraw()
        groupDrawer.preFrameDraw()

        renderInternal(frame, renderToSurface)

        textureDrawers.postFrameDraw()
        textureProcessors.postFrameDraw()
        groupDrawer.postFrameDraw()
    }

    fun dispose() {
        blender.dispose()
        textureDrawers.dispose()
        textureProcessors.dispose()
        groupDrawer.dispose()
        frameBufferPool.dispose()
    }

    protected fun renderInternal(frame: Frame, renderToSurface: Boolean) {
        val passFrameBuffers = frameBufferPool.acquireMany(PASS_FBO_NUM)

        if (frame.layers.isEmpty()) {
            GUtils.clear(frame.backgroundColor)
        } else {
            passFrameBuffers[0].withBind { GUtils.clear(frame.backgroundColor) }
        }

        frame.layers.forEachIndexed { index, layer ->
            val isLastPass = index == frame.layers.size - 1
            val lastFrameBuffer = passFrameBuffers[index % PASS_FBO_NUM]
            val currentFrameBuffer = passFrameBuffers[(index + 1) % PASS_FBO_NUM]

            val layerTexture = computeTextureInstruction(layer, lastFrameBuffer.texture)
            val maskTexture = layer.masking?.mask?.run {
                computeTextureInstruction(this, lastFrameBuffer.texture)
            }

            val bindReference = if (!isLastPass) currentFrameBuffer.bind() else null
            GUtils.clear()

            blender.blend(
                lastFrameBuffer.texture,
                layerTexture,
                layer.blending,
                maskTexture = maskTexture,
                maskBlending = layer.masking?.mask?.blending,
                maskType = layer.masking?.type,
                flipX = isLastPass && renderToSurface
            )

            bindReference?.unbind()
        }

        frameBufferPool.recycleMany(passFrameBuffers)
    }

    private fun computeTextureInstruction(
        layer: Layer,
        lastLayerTexture: GTexture
    ) : GTexture = when (layer.texturing) {
        is GroupInstruction -> groupDrawer.drawInternal(layer.texturing, layer.blending)
        is ProcessorInstruction -> textureProcessors.process(lastLayerTexture, layer.texturing)
        else -> textureDrawers.draw(layer.texturing, layer.blending)
    }

    private inner class GroupDrawer : TextureDrawer<GroupInstruction>() {
        override fun drawInternal(instruction: GroupInstruction, blending: BlenderInstruction): GTexture {
            val frameBuffer = frameBufferPool.acquire()
            frameBuffer.withBind {
                this@FrameCompositor.renderInternal(instruction.frame, false)
            }
            val texture = frameBuffer.texture.clone()
            frameBufferPool.recycle(frameBuffer)
            return texture
        }
    }

    companion object {
        private const val PASS_FBO_NUM = 3
    }
}
package com.colovaria.image_engine

import android.content.Context
import com.colovaria.geometry.Size
import com.colovaria.graphics.gles.GFrameBuffer
import com.colovaria.graphics.gles.GLESUtils
import com.colovaria.graphics.gles.GTexture
import com.colovaria.image_engine.api.Frame
import com.colovaria.image_engine.api.Layer
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.resources.ImageLoader
import com.colovaria.image_engine.api.texture.GroupInstruction
import com.colovaria.image_engine.api.texture.base.DrawerInstruction
import com.colovaria.image_engine.api.texture.base.ProcessorInstruction
import com.colovaria.image_engine.pipeline.drawers.Gradient2DDrawer
import com.colovaria.image_engine.pipeline.drawers.ImageDrawer
import com.colovaria.image_engine.pipeline.drawers.ShapeDrawer
import com.colovaria.image_engine.pipeline.drawers.TextDrawer
import com.colovaria.image_engine.pipeline.drawers.base.CachedTextureDrawer
import com.colovaria.image_engine.pipeline.drawers.base.CompositeTextureDrawer
import com.colovaria.image_engine.pipeline.processors.HSVProcessor
import com.colovaria.image_engine.pipeline.processors.base.CompositeTextureProcessor
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

    private val textureDrawers = CompositeTextureDrawer().apply {
        // Add defaults drawers:
        attachDrawer(ImageDrawer(imageLoader, size, optimizeImageSize))
        attachDrawer(Gradient2DDrawer(context, size))
        attachDrawer(ShapeDrawer(size))
        attachDrawer(TextDrawer(textMeasurer, fontProvider, size))
        attachDrawer(GroupDrawer())
    }
    private val textureProcessors = CompositeTextureProcessor(size).apply {
        // Add defaults drawers:
        attachProcessor(HSVProcessor(context, size))
    }

    init {
        GLESUtils.setViewportSize(size)
    }

    fun render(frame: Frame, renderToSurface: Boolean = true) {
        textureDrawers.preFrameDraw()
        textureProcessors.preFrameDraw()

        renderInternal(frame, renderToSurface)

        textureProcessors.postFrameDraw()
        textureDrawers.postFrameDraw()
    }

    fun dispose() {
        blender.dispose()
        textureDrawers.dispose()
        textureProcessors.dispose()
        frameBufferPool.dispose()
    }

    @Suppress("ProtectedInFinal")
    protected fun renderInternal(frame: Frame, renderToSurface: Boolean) : Unit = frameBufferPool.withMany(PASS_FBO_NUM) { passFrameBuffers ->
        if (frame.layers.isEmpty()) {
            GLESUtils.clear(frame.backgroundColor)
        } else {
            passFrameBuffers[0].withBind { GLESUtils.clear(frame.backgroundColor) }
        }

        frame.layers.forEachIndexed { index, layer ->
            val isLastPass = index == frame.layers.size - 1
            val lastFrameBuffer = passFrameBuffers[index % PASS_FBO_NUM]
            val currentFrameBuffer = passFrameBuffers[(index + 1) % PASS_FBO_NUM]

            val layerTexture = computeTextureInstruction(layer, lastFrameBuffer.texture!!)
            val maskTexture = layer.masking?.mask?.run {
                computeTextureInstruction(this, lastFrameBuffer.texture!!)
            }

            val bindReference = if (!isLastPass) currentFrameBuffer.bind() else null
            GLESUtils.clear()

            blender.blend(
                lastFrameBuffer.texture!!,
                layerTexture,
                layer.blending,
                maskTexture = maskTexture,
                maskBlending = layer.masking?.mask?.blending,
                maskType = layer.masking?.type,
                flipX = isLastPass && renderToSurface
            )

            bindReference?.unbind()
        }
    }

    private fun computeTextureInstruction(
        layer: Layer,
        lastLayerTexture: GTexture
    ) : GTexture = when (layer.texturing) {
        is DrawerInstruction -> textureDrawers.draw(layer.texturing, layer.blending)
        is ProcessorInstruction -> textureProcessors.process(layer.texturing, lastLayerTexture)
        else -> error("Unknown layer instruction ${layer.texturing}")
    }

    private inner class GroupDrawer : CachedTextureDrawer<GroupInstruction>() {
        override fun drawInternal(instruction: GroupInstruction, blending: BlenderInstruction): GTexture {
            return frameBufferPool.with { frameBuffer ->
                frameBuffer.withBind {
                    this@FrameCompositor.renderInternal(instruction.frame, false)
                }
                return@with frameBuffer.cloneTexture()
            }
        }
    }

    companion object {
        private const val PASS_FBO_NUM = 3
    }
}
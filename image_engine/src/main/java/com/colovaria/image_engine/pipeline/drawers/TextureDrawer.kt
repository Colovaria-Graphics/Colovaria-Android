package com.colovaria.image_engine.pipeline.drawers

import androidx.annotation.CallSuper
import com.colovaria.graphics.GTexture
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.texture.TextureInstruction

abstract class TextureDrawer<T: TextureInstruction>(
    private val useCache: Boolean = true
) {
    private val textureCache = mutableMapOf<T, GTexture>()
    private val currentFrameTextures = mutableListOf<T>()

    @CallSuper
    open fun preFrameDraw() {
        if (useCache) currentFrameTextures.clear()
    }

    fun draw(instruction: T, blending: BlenderInstruction): GTexture {
        if (useCache) {
            currentFrameTextures.add(instruction)
            if (textureCache.containsKey(instruction)) return textureCache[instruction]!!
        }

        val result = drawInternal(instruction, blending)

        if (useCache) {
            textureCache[instruction] = result
        }

        return result
    }

    @CallSuper
    open fun postFrameDraw() {
        if (useCache) {
            textureCache.keys.minus(currentFrameTextures).forEach {
                textureCache.remove(it)?.dispose()
            }
        }
    }

    @CallSuper
    open fun dispose() {
        if (useCache) {
            textureCache.forEach { it.value.dispose() }
            textureCache.clear()
        }
    }

    abstract fun drawInternal(instruction: T, blending: BlenderInstruction): GTexture
}
package com.colovaria.image_engine.pipeline.drawers.base

import androidx.annotation.CallSuper
import com.colovaria.graphics.gles.GTexture
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.texture.base.DrawerInstruction

/**
 * This class is an abstraction of TextureDrawer that store drawn textures in cache.
 * For example we might have a still-text texture, in this case there is no reason
 * to draw the whole text texture each frame.
 */
abstract class CachedTextureDrawer<T : DrawerInstruction> : TextureDrawer<T> {
    private val textureCache = mutableMapOf<T, GTexture>()
    private val currentFrameTextures = mutableListOf<T>()

    @CallSuper
    override fun preFrameDraw() {
        currentFrameTextures.clear()
    }

    @CallSuper
    override fun draw(instruction: T, blending: BlenderInstruction): GTexture {
        currentFrameTextures.add(instruction)
        if (textureCache.containsKey(instruction)) return textureCache[instruction]!!

        val result = drawInternal(instruction, blending)

        textureCache[instruction] = result

        return result
    }

    @CallSuper
    override fun postFrameDraw() {
        textureCache.keys.minus(currentFrameTextures.toSet()).forEach {
            textureCache.remove(it)?.dispose()
        }
    }

    @CallSuper
    override fun dispose() {
        textureCache.forEach { it.value.dispose() }
        textureCache.clear()
    }

    abstract fun drawInternal(instruction: T, blending: BlenderInstruction): GTexture
}
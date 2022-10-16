package com.colovaria.image_engine.pipeline.drawers.base

import com.colovaria.graphics.gles.GTexture
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.texture.base.DrawerInstruction
import kotlin.reflect.KClass

class CompositeTextureDrawer : TextureDrawer<DrawerInstruction> {
    private val drawers = mutableMapOf<KClass<out DrawerInstruction>, TextureDrawer<in DrawerInstruction>>()

    fun <I: DrawerInstruction, D : TextureDrawer<I>> attachDrawer(instructionClass: KClass<I>, textureDrawer: D) {
        require(!drawers.containsKey(instructionClass)) { "Drawer already exists for $instructionClass" }
        @Suppress("UNCHECKED_CAST")
        drawers[instructionClass] = textureDrawer as TextureDrawer<in DrawerInstruction>
    }

    inline fun <reified I : DrawerInstruction, D : TextureDrawer<I>> attachDrawer(textureDrawer: D) {
        attachDrawer(I::class, textureDrawer)
    }

    fun detachDrawer(instructionClass: KClass<DrawerInstruction>) {
        require(drawers.containsKey(instructionClass))
        drawers.remove(instructionClass)
    }

    fun hasDrawer(instructionClass: KClass<DrawerInstruction>) : Boolean {
        return drawers.containsKey(instructionClass)
    }

    override fun preFrameDraw() {
        drawers.values.forEach { it.preFrameDraw() }
    }

    override fun draw(instruction: DrawerInstruction, blending: BlenderInstruction) : GTexture {
        return drawers[instruction::class]?.draw(instruction, blending)
            ?: error("No matching drawer for $instruction")
    }

    override fun postFrameDraw() {
        drawers.values.forEach { it.postFrameDraw() }
    }

    override fun dispose() {
        drawers.values.forEach { it.dispose() }
    }
}

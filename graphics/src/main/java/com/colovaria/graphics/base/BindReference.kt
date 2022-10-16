package com.colovaria.graphics.base

/**
 * This class represent a bind relation that could be unbind.
 */
class BindReference(private val unbindFunc: () -> Unit) {
    fun unbind() = unbindFunc.invoke()
}

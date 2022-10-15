package com.colovaria.graphics

/**
 * This class represent a bind relation that could be unbind.
 */
class BindReference(private val unbindFunc: () -> Unit) {
    fun unbind() = unbindFunc.invoke()
}

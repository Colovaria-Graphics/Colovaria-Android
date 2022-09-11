package com.graphics

class BindReference(private val unbindFunc: () -> Unit) {
    fun unbind() = unbindFunc.invoke()
}
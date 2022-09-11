package com.colovaria.graphics

class BindReference(private val unbindFunc: () -> Unit) {
    fun unbind() = unbindFunc.invoke()
}
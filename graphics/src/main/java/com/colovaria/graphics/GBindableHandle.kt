package com.colovaria.graphics

interface GBindableHandle {
    /**
     * This function bind the current object handle and
     * return the handle of the last bounded object.
     */
    fun bind() : BindReference
}

/**
 * This extension function execute safely a given function under a bind relation.
 */
inline fun <T> GBindableHandle.withBind(func: () -> T) : T {
    val bindReference = bind()
    try {
        return func()
    } finally {
        bindReference.unbind()
    }
}
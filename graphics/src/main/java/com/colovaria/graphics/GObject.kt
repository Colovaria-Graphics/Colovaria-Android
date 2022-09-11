package com.colovaria.graphics

abstract class GObject {
    @Volatile
    protected var disposed = false

    abstract fun dispose()

    fun finalize() {
        assert(disposed) { toString() }
    }
}
package com.colovaria.graphics.base

abstract class GObject {
    @Volatile
    private var disposed = false

    protected abstract fun actualDispose()

    fun isDispose() = disposed

    fun dispose() {
        actualDispose()
        disposed = true
    }

    fun finalize() {
        assert(disposed) { toString() }
    }
}

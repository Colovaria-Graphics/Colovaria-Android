package com.colovaria.image_engine.utils

class ObjectPool<T>(
    private val factory: () -> T,
    private val cleaner: (T) -> Unit = {},
    private val disposer: (T) -> Unit = {},
    initialAmount: Int = 0
) {
    private val pool = mutableListOf<T>()

    init {
        repeat(initialAmount) { pool.add(factory()) }
    }

    fun acquire() : T {
        return if (pool.isEmpty()) factory() else pool.removeAt(0)
    }

    fun acquireMany(count: Int) : List<T> {
        return (0 until count).map { acquire() }
    }

    fun recycle(t: T) {
        cleaner(t)
        pool.add(t)
    }

    fun recycleMany(list: List<T>) {
        list.forEach { recycle(it) }
    }

    fun dispose() {
        pool.forEach(disposer)
        pool.clear()
    }
}
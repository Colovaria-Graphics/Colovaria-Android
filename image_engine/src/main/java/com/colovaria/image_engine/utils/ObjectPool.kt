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

    fun <R> with(func: (T) -> R) : R {
        val t = acquire()
        try {
            return func(t)
        } finally {
            recycle(t)
        }
    }

    fun <R> withMany(count: Int, func: (List<T>) -> R) : R {
        val t = acquireMany(count)
        try {
            return func(t)
        } finally {
            recycleMany(t)
        }
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
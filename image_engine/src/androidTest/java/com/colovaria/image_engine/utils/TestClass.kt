package com.colovaria.image_engine.utils

import androidx.test.platform.app.InstrumentationRegistry

open class TestClass {
    val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }
}
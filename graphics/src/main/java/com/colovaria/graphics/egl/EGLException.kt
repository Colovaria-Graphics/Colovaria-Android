package com.colovaria.graphics.egl

data class EGLException(
    val glCode: Int
) : Exception("OpenGLES Exception ${exceptionIntToCode(glCode)}") {
    companion object {
        fun exceptionIntToCode(errorCode: Int) = "0x${errorCode.toString(16)}"
    }
}

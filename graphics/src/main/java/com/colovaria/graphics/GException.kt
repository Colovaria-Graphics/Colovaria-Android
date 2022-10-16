package com.colovaria.graphics

data class GException(
    val glCode: Int
) : Exception("OpenGLES Exception ${exceptionIntToCode(glCode)}") {
    companion object {
        fun exceptionIntToCode(errorCode: Int) = "0x${errorCode.toString(16)}"
    }
}

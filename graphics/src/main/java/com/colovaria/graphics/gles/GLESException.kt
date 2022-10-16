package com.colovaria.graphics.gles

data class GLESException(
    val glCode: Int
) : Exception("OpenGLES Exception ${exceptionIntToCode(glCode)}") {
    companion object {
        fun exceptionIntToCode(errorCode: Int) = "0x${errorCode.toString(16)}"
    }
}

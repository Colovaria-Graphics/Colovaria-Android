package com.colovaria.graphics

import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLDisplay
import com.colovaria.graphics.egl.GEGLUtils

data class GDisplayConfigSpec(
    val redSize: Int        = 8,
    val greenSize: Int      = 8,
    val blueSize: Int       = 8,
    val alphaSize: Int      = 0,
    val depthSize: Int      = 0,
    val stencilSize: Int    = 0,
) {
    private val colorAttribute = arrayOf(redSize, greenSize, blueSize, alphaSize)

    fun toIntArray() = intArrayOf(
        EGL14.EGL_RED_SIZE, redSize,
        EGL14.EGL_GREEN_SIZE, greenSize,
        EGL14.EGL_BLUE_SIZE, blueSize,
        EGL14.EGL_ALPHA_SIZE, alphaSize,
        EGL14.EGL_DEPTH_SIZE, depthSize,
        EGL14.EGL_STENCIL_SIZE, stencilSize,
        EGL14.EGL_NONE
    )

    /**
     * This function compute the mathematical distance between the current spec to the spec
     * of a given display.
     * This function will return null in case the given spec don't match the current spec.
     * Otherwise it will return an int that represent the distance between the specs.
     * This could be use in order to find the most similar spec, lower return value means more
     * similar specs.
     */
    fun computeMatchDistance(display: EGLDisplay, eglConfig: EGLConfig) : Int? {
        val tempArray = IntArray(1)

        fun findAttribute(attribute: Int) : Int {
            return GEGLUtils.findConfigAttrib(display, eglConfig, attribute, tempArray=tempArray)
        }

        val displayColorAttribute = arrayOf(
            findAttribute(EGL14.EGL_RED_SIZE),
            findAttribute(EGL14.EGL_GREEN_SIZE),
            findAttribute(EGL14.EGL_BLUE_SIZE),
            findAttribute(EGL14.EGL_ALPHA_SIZE)
        )

        if (!colorAttribute.contentEquals(displayColorAttribute)) return null

        val displayDepthSize = findAttribute(EGL14.EGL_DEPTH_SIZE)
        val displayStencilSize = findAttribute(EGL14.EGL_STENCIL_SIZE)

        if (displayDepthSize < depthSize || displayStencilSize < stencilSize) return null

        return (displayDepthSize - depthSize) + (displayStencilSize - stencilSize)
    }
}
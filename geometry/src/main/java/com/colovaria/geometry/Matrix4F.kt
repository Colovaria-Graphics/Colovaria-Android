package com.colovaria.geometry

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

data class Matrix4F(
    val mat: List<Float>,
) {
    operator fun times(rhs: Matrix4F) : Matrix4F {
        return multiply(this, rhs)
    }

    fun rotate(degrees: Float, x: Float, y: Float, z: Float) : Matrix4F {
        return this * Matrix4F.rotate(degrees, x, y, z)
    }

    fun scale(x: Float, y: Float, z: Float) : Matrix4F {
        return this * Matrix4F.scale(x, y, z)
    }

    fun translate(x: Float, y: Float, z: Float) : Matrix4F {
        return this * Matrix4F.translate(x, y, z)
    }

    companion object {
        fun identity() : Matrix4F {
            return Matrix4F(listOf(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f,
            ))
        }

        fun rotate(degrees: Float, x: Float, y: Float, z: Float) : Matrix4F {
            val radians = (degrees * Math.PI / 180f).toFloat()
            val c = cos(radians)
            val s = sin(radians)

            val nc = 1.0f - c
            val xy = x * y
            val yz = y * z
            val zx = z * x
            val xs = x * s
            val ys = y * s
            val zs = z * s

            return Matrix4F(listOf(
                x * x * nc + c, xy * nc + zs,   zx * nc - ys,   0f,
                xy * nc - zs,   y * y * nc + c, yz * nc + xs,   0f,
                zx * nc + ys,   yz * nc - xs,   z * z * nc + c, 0f,
                0f,             0f,             0f,             1f,
            ))
        }

        fun scale(x: Float, y: Float, z: Float) : Matrix4F {
            return Matrix4F(listOf(
                x,  0f, 0f, 0f,
                0f, y,  0f, 0f,
                0f, 0f, z,  0f,
                0f, 0f, 0f, 1f,
            ))
        }

        fun translate(x: Float, y: Float, z: Float) : Matrix4F {
            return Matrix4F(listOf(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                x,  y,  z,  1f,
            ))
        }

        fun ortho(l: Float, r: Float, b: Float, t: Float, n: Float, f: Float) : Matrix4F {
            return Matrix4F(listOf(
                2 / (r - l),         0f,                  0f,                  0f,
                0f,                  2 / (t - b),         0f,                  0f,
                0f,                  0f,                  -2 / (f - n),        0f,
                -(r + l) / (r - l),  -(t + b) / (t - b),  -(f + n) / (f - n),  1f,
            ))
        }

        fun orthoWindow(width: Float, height: Float) : Matrix4F {
            return ortho(0f, width, height, 0f, -1f, 1f)
        }

        fun frustum(l: Float, r: Float, b: Float, t: Float, n: Float, f: Float) : Matrix4F {
            return Matrix4F(listOf(
                2 * n / (r - l),   0f,                0f,                  0f,
                0f,                2 * n / (t - b),   0f,                  0f,
                (r + l) / (r - l), (t + b) / (t - b), -(f + n) / (f - n), -1f,
                0f,                0f,                -2*f*n / (f - n),    0f,
            ))
        }

        fun perspective(fovy: Float, aspect: Float, near: Float, far: Float) : Matrix4F {
            val top = near * tan(fovy * Math.PI / 360.0f).toFloat()
            val bottom = -top
            val left = bottom * aspect
            val right = top * aspect
            return frustum(left, right, bottom, top, near, far)
        }

        fun multiply(lhs: Matrix4F, rhs: Matrix4F) : Matrix4F {
            val matrix = MutableList(16) { 0f }

            for (i in 0 until 4) {
                for (j in 0 until 4) {
                    for (h in 0 until 4) {
                        matrix[i * 4 + j] += lhs.mat[i * 4 + h] * rhs.mat[h * 4 + j]
                    }
                }
            }

            return Matrix4F(matrix.toList())
        }
    }
}
package com.colovaria.geometry

import org.junit.Test

class Matrix4FTest {
    @Test
    fun testTimes1() {
        val mat1 = Matrix4F(listOf(6.08f, 6.21f, 4.69f, 6.17f, 4.61f, 5.17f, 3.36f, 7.52f, 5.05f, 0.12f, 6.08f, 8.05f, 6.49f, 6.7f, 6.33f, 6.1f))
        val mat2 = Matrix4F(listOf(3.64f, 2.76f, 6.93f, 5.98f, 5.54f, 1.56f, 2.03f, 4.58f, 6.54f, 4.9f, 5.59f, 2.52f, 2.58f, 5.12f, 4.55f, 9.37f))
        val expected = Matrix4F(listOf(103.1258f, 81.0398f, 109.0313f, 134.4319f, 86.7982f, 75.7552f, 95.4408f, 130.176f, 79.579f, 85.1332f, 105.8548f, 121.4987f, 117.8778f, 90.6134f, 121.7164f, 142.6048f))

        val result = mat1 * mat2

        assert(result == expected)
    }

    @Test
    fun testTimes2() {
        val mat1 = Matrix4F.identity()
        val mat2 = Matrix4F.identity()
        val expected = Matrix4F.identity()

        val result = mat1 * mat2

        assert(result == expected)
    }

    @Test
    fun testTimes3() {
        val mat1 = Matrix4F.translate(1f, 2f, 3f)
        val mat2 = Matrix4F.translate(11f, 12f, 13f)
        val expected = Matrix4F.translate(12f, 14f, 16f)

        val result = mat1 * mat2

        assert(result == expected)
    }

    @Test
    fun testTimes4() {
        val mat1 = Matrix4F.rotate(90f, 0f, 0f, 1f)
        val mat2 = Matrix4F.rotate(5f, 0f, 0f, 1f)
        val expected = Matrix4F.rotate(95f, 0f, 0f, 1f)

        val result = mat1 * mat2

        assert(result == expected)
    }

    @Test
    fun testTimes5() {
        val mat1 = Matrix4F.scale(1f, 2f, 3f)
        val mat2 = Matrix4F.scale(2f, 3f, 4f)
        val expected = Matrix4F.scale(2f, 6f, 12f)

        val result = mat1 * mat2

        assert(result == expected)
    }

    @Test
    fun testTimes6() {
        val mat1 = Matrix4F.rotate(90f, 0f, 0f, 1f)
        val mat2 = Matrix4F.identity()
        val expected = Matrix4F.rotate(90f, 0f, 0f, 1f)

        val result = mat1 * mat2

        assert(result == expected)
    }
}
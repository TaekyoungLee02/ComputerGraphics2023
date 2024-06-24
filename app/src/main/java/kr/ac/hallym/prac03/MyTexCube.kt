package kr.ac.hallym.prac03

import android.content.Context

class MyTexCube : ShapeInfo()
{

    override val shapeName = "MyTexCube"

    override val vertexCoords = floatArrayOf( //in counterclockwise(반시계방향) order:
        -0.5f, -0.5f, -0.5f,
        -0.5f, 0.5f, -0.5f,
        -0.5f, -0.5f, 0.5f,
        -0.5f, 0.5f, 0.5f,
        0.5f, -0.5f, -0.5f,
        0.5f, 0.5f, -0.5f,
        0.5f, -0.5f, 0.5f,
        0.5f, 0.5f, 0.5f,
        -0.5f, 0.5f, -0.5f,
        -0.5f, 0.5f, 0.5f,
        0.5f, 0.5f, -0.5f,
        0.5f, 0.5f, 0.5f,
        0.5f, 0.5f, -0.5f,
        0.5f, 0.5f, 0.5f,
        )

    override var vertexUVs = floatArrayOf(
        0.5f, 0.3333f,
        0.5f, 0.0f,
        0.5f, 0.6667f,
        0.5f, 1.0f,
        0.75f, 0.3333f,
        0.75f, 0.0f,
        0.75f, 0.6667f,
        0.75f, 1.0f,
        0.25f, 0.3333f,
        0.25f, 0.6667f,
        0.0f, 0.3333f,
        0.0f, 0.6667f,
        1.0f, 0.3333f,
        1.0f, 0.6667f,
    )

    override val drawOrder = shortArrayOf(
        0, 4, 6, 0, 6, 2,
        0, 1, 5, 0, 5, 4,
        8, 0, 9, 9, 0, 2,
        4, 13, 6, 4, 12, 13,
        6, 7, 3, 6, 3, 2,
        8, 10, 11, 8, 11, 9
    )

    override var bitmapName: String = "crate_new.bmp"

    override var useTexture = true

    override var vPosition = 8
    override var vTexCoord = 9
}
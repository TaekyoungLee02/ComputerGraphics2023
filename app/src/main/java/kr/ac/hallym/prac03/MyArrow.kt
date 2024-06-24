package kr.ac.hallym.prac03

class MyArrow : ShapeInfo() {

    override val shapeName = "MyArrow"

    override val vertexCoords = floatArrayOf(
        -1.0f, 0.1f, 0.1f,
        -1.0f, 0.1f, -0.1f,
        -1.0f, -0.1f, -0.1f,
        -1.0f, -0.1f, 0.1f,

        0.7f, 0.1f, 0.1f,
        0.7f, 0.1f, -0.1f,
        0.7f, -0.1f, -0.1f,
        0.7f, -0.1f, 0.1f,

        0.5f, 0.5f, 0.1f,
        0.7f, 0.5f, 0.1f,
        0.7f, 0.5f, -0.1f,
        0.5f, 0.5f, -0.1f,

        0.5f, -0.5f, 0.1f,
        0.5f, -0.5f, -0.1f,
        0.7f, -0.5f, -0.1f,
        0.7f, -0.5f, 0.1f,

        1.0f, 0.0f, 0.1f,
        1.0f, 0.0f, -0.1f
        )

    override val drawOrder = shortArrayOf(
        0, 1, 2, 0, 2, 3,
        1, 5, 4, 1, 4, 2,
        5, 17, 6,
        11, 10, 17, 11, 17, 5,
        6, 17, 13, 17, 14, 13,
        0, 4, 5, 0, 5, 1,
        4, 8, 11, 4, 11, 5,
        8, 9, 10, 8, 10, 11,
        9, 16, 17, 9, 17, 10,
        15, 14, 17, 15, 17, 16,
        12, 13, 14, 12, 14, 15,
        6, 13, 12, 6, 12, 7,
        2, 6, 7, 2, 7, 3,
        0, 3, 7, 0, 7, 4,
        4, 7, 16,
        4, 16, 9, 4, 9, 8,
        7, 12, 15, 7, 15, 16
    )

    override var vertexColors = floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f)

    override var useUniformColor = true

    override var vPosition = 1
}
package kr.ac.hallym.prac03

class MyStar : ShapeInfo(){

    override val shapeName = "MyStar"

    override val vertexCoords = floatArrayOf(
        0.0f, 0.0f, -0.5f,
        0.0f, 0.0f, 0.5f,
        0.0f, 1.0f, 0.0f,
        -0.95f, 0.3f, 0.0f,
        -0.225f, 0.3f, 0.0f,
        0.225f, 0.3f, 0.0f,
        0.95f, 0.3f, 0.0f,
        -0.375f, -0.12f, 0.0f,
        0.375f, -0.12f, 0.0f,
        0.0f, -0.38f, 0.0f,
        -0.6f, -0.8f, 0.0f,
        0.6f, -0.8f, 0.0f,
    )
    override val drawOrder = shortArrayOf(
        0, 2, 4, 0, 5, 2,
        0, 4, 3, 0, 3, 7,
        0, 7, 10, 0, 10, 9,
        0, 9, 11, 0, 11, 8,
        0, 8, 6, 0, 6, 5,
        1, 2, 4, 1, 5, 2,
        1, 4, 3, 1, 3, 7,
        1, 7, 10, 1, 10, 9,
        1, 9, 11, 1, 11, 8,
        1, 8, 6, 1, 6, 5
    )


    override var vertexColors = floatArrayOf(1.0f, 0.0f, 1.0f, 1.0f)

    override var useUniformColor = true

    override var vPosition = 0
}
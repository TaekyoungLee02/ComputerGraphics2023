package kr.ac.hallym.prac03

class MyTetra : ShapeInfo() {

    override val shapeName = "MyTetra"

    override val vertexCoords = floatArrayOf(
        1.0f, -0.8164f, -0.866f,
        -1.0f, -0.8164f, -0.866f,
        0.0f, -0.8164f, 0.866f,
        0.0f, 0.8164f, 0.0f
    )
    override val drawOrder = shortArrayOf(
        0, 1, 2,
        0, 1, 3,
        1, 2, 3,
        0, 3, 2
    )

    override var vertexColors = floatArrayOf(0.0f, 1.0f, 1.0f, 1.0f)

    override var useUniformColor = true

    override var vPosition = 0

}
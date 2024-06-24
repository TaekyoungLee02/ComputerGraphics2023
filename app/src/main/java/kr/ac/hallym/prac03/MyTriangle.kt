package kr.ac.hallym.prac03

class MyTriangle : ShapeInfo() {

    override val shapeName = "MyTriangle"

    override val vertexCoords = floatArrayOf( //in counterclockwise(반시계방향) order:
        0.0f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
    )

    override val drawOrder = shortArrayOf(0, 1, 2)

    override var vertexColors = floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f)

    override var useUniformColor = true

    override var vPosition = 2
}
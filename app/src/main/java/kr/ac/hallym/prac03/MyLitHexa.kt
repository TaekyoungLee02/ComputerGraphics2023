package kr.ac.hallym.prac03

class MyLitHexa : ShapeInfo() {

    override val shapeName = "MyLitHexa"

    override val vertexCoords = floatArrayOf(
        //in counterclockwise(반시계방향) order:
        0.0f, 0.5f, 0.0f, // W
        1.0f, -1.0f, 0.0f, // R
        0.5f, -1.0f, -0.866f, // Y
        -0.5f, -1.0f, -0.866f, // G
        -1.0f, -1.0f, 0.0f, // C
        -0.5f, -1.0f, 0.866f, // B
        0.5f, -1.0f, 0.866f, // M
    )
    override var vertexNormals = floatArrayOf(
        0.0f, 1.0f, 0.0f, // W
        1.0f, 0.0f, 0.0f, // R
        0.5f, 0.0f, -0.866f, // Y
        -0.5f, 0.0f, -0.866f, // G
        -1.0f, 0.0f, 0.0f, // C
        -0.5f, 0.0f, 0.866f, // B
        0.5f, 0.0f, 0.866f, // M
    )
    override val drawOrder = shortArrayOf(
        0, 1, 2, 0, 2, 3,
        0, 3, 4, 0, 4, 5,
        0, 5, 6, 0, 6, 1
    )

    override var vertexColors = floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f)

    override var matAmbient = floatArrayOf(1.0f, 1.0f, 1.0f)
    override var matSpecular = floatArrayOf(1.0f, 1.0f, 1.0f)
    override var matShininess = 10.0f

    override var useLight = true
    override var useUniformColor = true

    override var vPosition = 3
    override var vNormal = 4
}
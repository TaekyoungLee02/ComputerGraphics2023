package kr.ac.hallym.prac03

class MyLitTetra : ShapeInfo() {

    override val shapeName = "MyLitTetra"

    override val vertexCoords = floatArrayOf(
        1.0f, -0.8164f, -0.866f,
        -1.0f, -0.8164f, -0.866f,
        0.0f, -0.8164f, 0.866f,
        0.0f, 0.8164f, 0.0f
    )

    override var vertexNormals = floatArrayOf(
        0.643294f, -0.525186f, -0.557093f,
        -0.643294f, -0.525186f, -0.557093f,
        0.0f, -0.685962f, 0.727637f,
        0.0f, 1.0f, 0.0f
    )

    override val drawOrder = shortArrayOf(
        0, 1, 2,
        0, 1, 3,
        1, 2, 3,
        0, 3, 2
    )

    override var matAmbient = floatArrayOf(1.0f, 1.0f, 1.0f)
    override var matSpecular = floatArrayOf(1.0f, 1.0f, 1.0f)
    override var matShininess = 10.0f

    override var vertexColors = floatArrayOf(1.0f, 1.0f, 0.0f, 1.0f)

    override var useUniformColor = true
    override var useLight = true

    override var vPosition = 8
    override var vNormal = 9
}
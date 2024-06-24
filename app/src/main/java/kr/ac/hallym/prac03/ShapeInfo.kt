package kr.ac.hallym.prac03

abstract class ShapeInfo
{
    abstract val shapeName : String

    abstract val vertexCoords : FloatArray;
    open var vertexColors = floatArrayOf(0f);
    open var vertexNormals = floatArrayOf(0f);
    open var vertexUVs = floatArrayOf(0f);

    abstract val drawOrder : ShortArray;

    open var matAmbient = floatArrayOf(0f)
    open var matSpecular = floatArrayOf(0f)
    open var matShininess = 0f

    open var bitmapName = ""

    open var useFragColor = false;
    open var useUniformColor = false;
    open var useLight = false;
    open var useTexture = false;

    open var vPosition = -1;
    open var vColor = -1;
    open var vNormal = -1;
    open var vTexCoord = -1;
}
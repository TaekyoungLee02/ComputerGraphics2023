package kr.ac.hallym.prac03

import android.opengl.GLES30
import java.nio.FloatBuffer

abstract class ShapeBase
{
    protected abstract val vertexCoords : FloatArray

    protected abstract var vertexBuffer : FloatBuffer

    public abstract val shapeName : String

    protected var mProgram: Int = -1

    protected val vertexStride: Int = COORDS_PER_VERTEX * 4 // 3 * 4 == 12
    protected var vertexCount : Int = -1
    protected var mvpMatrixHandle: Int = -1 // use to access and set the transform matrix
    protected var mWorldMatHandle: Int = -1


    protected abstract val vertexShader : Int
    protected abstract val fragmentShader: Int

    open fun draw(mvpMatrix: FloatArray, worldMat:FloatArray)
    {
        GLES30.glUseProgram(mProgram)

        GLES30.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)
        GLES30.glUniformMatrix4fv(mWorldMatHandle, 1, false, worldMat, 0)
    }
}
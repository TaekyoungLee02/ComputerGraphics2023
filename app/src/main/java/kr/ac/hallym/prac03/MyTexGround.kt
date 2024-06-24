package kr.ac.hallym.prac03

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLUtils
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class MyTexGround(val myContext: Context) {

    private val vertexCoords = floatArrayOf(
        -10.0f, -1.0f, -10.0f,
        -10.0f, -1.0f, 10.0f,
        10.0f, -1.0f, 10.0f,
        -10.0f, -1.0f, -10.0f,
        10.0f, -1.0f, 10.0f,
        10.0f, -1.0f, -10.0f
    )

    private val vertexUVs = floatArrayOf(
        0.0f, 0.0f,
        0.0f, 20.0f,
        20.0f, 20.0f,
        0.0f, 0.0f,
        20.0f, 20.0f,
        20.0f, 0.0f
    )

    private val vertexNormals = floatArrayOf(
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f
    )

    private var matAmbient = floatArrayOf(1.0f, 1.0f, 1.0f)
    private var matSpecular = floatArrayOf(1.0f, 1.0f, 1.0f)
    private var matShininess = 1.0f

    private var mWorldMatHandle: Int = -1
    private var mLightAmbiHandle: Int = -1
    private var mLightDiffHandle: Int = -1
    private var mLightDirHandle: Int = -1
    private var mLightSpecHandle: Int = -1
    private var mMatAmbiHandle: Int = -1
    private var mMatSpecHandle: Int = -1
    private var mMatShHandle: Int = -1
    private var mEyePosHandle: Int = -1

    private var vertexBuffer: FloatBuffer =
        // (number of coordinate(좌표) values * 4 bytes per float)
        ByteBuffer.allocateDirect(vertexCoords.size * 4).run {

            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())

            // create a floating point buffer from the ByteBuffer
            asFloatBuffer().apply {
                // add the coordinates to the FloatBuffer
                put(vertexCoords)
                // set the buffer to read the first coordinate
                position(0)
            }
        }
    private var normalBuffer: FloatBuffer =
        // (number of coordinate(좌표) values * 4 bytes per float)
        ByteBuffer.allocateDirect(vertexNormals.size * 4).run {

            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())

            // create a floating point buffer from the ByteBuffer
            asFloatBuffer().apply {
                // add the coordinates to the FloatBuffer
                put(vertexNormals)
                // set the buffer to read the first coordinate
                position(0)
            }
        }

    private var uvBuffer: FloatBuffer =
        // (number of coordinate(좌표) values * 4 bytes per float)
        ByteBuffer.allocateDirect(vertexUVs.size * 4).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())

            // create a floating point buffer from the ByteBuffer
            asFloatBuffer().apply {
                // add the coordinates to the FloatBuffer
                put(vertexUVs)
                // set the buffer to read the first coordinate
                position(0)
            }
        }

    private var mProgram: Int = -1

    private var mvpMatrixHandle: Int = -1

    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 3 * 4 == 12
    private var vertexCount : Int = vertexCoords.size / COORDS_PER_VERTEX

    private var textureID = IntArray(1)

    init {
        val vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, "fog_ground_light_tex_vert.glsl", myContext)
        val fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, "fog_ground_light_tex_frag.glsl", myContext)

        mProgram = GLES30.glCreateProgram().also {
            GLES30.glAttachShader(it, vertexShader)
            GLES30.glAttachShader(it, fragmentShader)
            GLES30.glLinkProgram(it)
        }

        GLES30.glUseProgram(mProgram)

        GLES30.glEnableVertexAttribArray(10)
        GLES30.glVertexAttribPointer(
            10, COORDS_PER_VERTEX,
            GLES30.GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer
        )

        GLES30.glEnableVertexAttribArray(11)
        GLES30.glVertexAttribPointer(
            11,
            2,
            GLES30.GL_FLOAT,
            false,
            0,
            uvBuffer
        )
        GLES30.glEnableVertexAttribArray(12)
        // Prepare the triangle coordinate data
        GLES30.glVertexAttribPointer (
            12,
            COORDS_PER_VERTEX,
            GLES30.GL_FLOAT,
            false,
            vertexStride,
            normalBuffer
        )

        mEyePosHandle = GLES30.glGetUniformLocation(mProgram, "eyePos").also {
            GLES30.glUniform3fv(it, 1, eyePos, 0)
        }
        Log.d("mEyePosHandle", mEyePosHandle.toString())
        mLightAmbiHandle = GLES30.glGetUniformLocation(mProgram, "lightAmbi").also {
            GLES30.glUniform3fv(it, 1, lightAmbient, 0)
        }
        Log.d("mLightAmbiHandle", mLightAmbiHandle.toString())

        mLightDirHandle = GLES30.glGetUniformLocation(mProgram, "lightDir").also {
            GLES30.glUniform3fv(it, 1, lightDir, 0)
        }
        Log.d("mLightDirHandle", mLightDirHandle.toString())

        mLightSpecHandle = GLES30.glGetUniformLocation(mProgram, "lightSpec").also {
            GLES30.glUniform3fv(it, 1, lightSpecular, 0)
        }
        Log.d("mLightSpecHandle", mLightSpecHandle.toString())

        mLightDiffHandle = GLES30.glGetUniformLocation(mProgram, "lightDiff").also {
            GLES30.glUniform3fv(it, 1, lightDiffuse, 0)
        }
        Log.d("mLightDiffHandle", mLightDiffHandle.toString())


        mMatAmbiHandle = GLES30.glGetUniformLocation(mProgram, "matAmbi").also {
            GLES30.glUniform3fv(it, 1, matAmbient, 0)
        }
        Log.d("mMatAmbiHandle", mMatAmbiHandle.toString())

        mMatSpecHandle = GLES30.glGetUniformLocation(mProgram, "matSpec").also {
            GLES30.glUniform3fv(it, 1, matSpecular, 0)
        }
        Log.d("mMatSpecHandle", mMatSpecHandle.toString())

        mMatShHandle = GLES30.glGetUniformLocation(mProgram, "matSh").also {
            GLES30.glUniform1f(it, matShininess)
        }
        GLES30.glUniform1f(GLES30.glGetUniformLocation(mProgram, "fogStart"), 5.0f)
        GLES30.glUniform1f(GLES30.glGetUniformLocation(mProgram, "fogEnd"), 10.0f)
        GLES30.glUniform3f(GLES30.glGetUniformLocation(mProgram, "fogColor"), 0.2f, 0.2f, 0.2f)

        mvpMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix")
        mWorldMatHandle = GLES30.glGetUniformLocation(mProgram, "worldMat")

        GLES30.glGenTextures(1, textureID, 0)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureID[0])
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)

        //GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_MIRRORED_REPEAT)
        //GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_MIRRORED_REPEAT)
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, loadBitmap("ground.bmp", myContext), 0)
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D)
    }

    fun draw(mvpMatrix: FloatArray, worldMat : FloatArray)
    {
        GLES30.glUseProgram(mProgram)

        GLES30.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)
        GLES30.glUniformMatrix4fv(mWorldMatHandle, 1, false, worldMat, 0)

        GLES30.glUniform3fv(mEyePosHandle, 1, eyePos, 0)

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureID[0])

        // Draw the triangle
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount)
    }
}
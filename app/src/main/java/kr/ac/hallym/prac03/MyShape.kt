package kr.ac.hallym.prac03

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLUtils
import android.util.Log
import java.io.BufferedInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class MyShape constructor (shapeInfo: ShapeInfo, val myContext: Context) : ShapeBase() {

    override val shapeName : String

    // Receive from ShapeInfo
    override val vertexCoords : FloatArray
    private val vertexColors : FloatArray
    private val vertexUVs : FloatArray
    private val vertexNormals : FloatArray
    private val matAmbient : FloatArray
    private val matSpecular : FloatArray
    private val matShininess : Float
    private val drawOrder : ShortArray


    // 배열로 메모리를 할당했는데 한번 더 버퍼로 할당하는 이유 : cpu 와 gpu 가 메모리를 읽는 순서가 달라서..
    override lateinit var vertexBuffer : FloatBuffer
    private var colorBuffer : FloatBuffer?
    private var normalBuffer : FloatBuffer?
    private var uvBuffer : FloatBuffer?

    private var textureID = IntArray(1)

    //    private var colorBuffer : FloatBuffer
    private val indexBuffer : ShortBuffer

    override val vertexShader : Int
    override val fragmentShader : Int

    private var mColorHandle: Int = -1
    private var mLightAmbiHandle: Int = -1
    private var mLightDiffHandle: Int = -1
    private var mLightDirHandle: Int = -1
    private var mLightSpecHandle: Int = -1
    private var mMatAmbiHandle: Int = -1
    private var mMatSpecHandle: Int = -1
    private var mMatShHandle: Int = -1
    private var mEyePosHandle: Int = -1

    init // constructor
    {
        /************* initiate Buffers *****************/

        this.shapeName = shapeInfo.shapeName
        this.vertexCoords = shapeInfo.vertexCoords
        this.vertexColors = shapeInfo.vertexColors
        this.vertexNormals = shapeInfo.vertexNormals
        this.matAmbient = shapeInfo.matAmbient
        this.matSpecular = shapeInfo.matSpecular
        this.matShininess = shapeInfo.matShininess
        this.drawOrder = shapeInfo.drawOrder
        this.vertexUVs = shapeInfo.vertexUVs

        vertexBuffer =
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

        indexBuffer =
                // (number of index values * 2 bytes per short)
            ByteBuffer.allocateDirect(drawOrder.size * 2).run {
                order(ByteOrder.nativeOrder())
                asShortBuffer().apply {
                    put(drawOrder)
                    position(0)
                }
            }

        // If Use FragColor
        if(shapeInfo.useFragColor)
        {
            colorBuffer =
                    // (number of coordinate(좌표) values * 4 bytes per float)
                ByteBuffer.allocateDirect(vertexColors.size * 4).run {
                    // use the device hardware's native byte order
                    order(ByteOrder.nativeOrder())

                    // create a floating point buffer from the ByteBuffer
                    asFloatBuffer().apply {
                        // add the coordinates to the FloatBuffer
                        put(vertexColors)
                        // set the buffer to read the first coordinate
                        position(0)
                    }
                }
        }
        else colorBuffer = null

        // if Use Light
        if(shapeInfo.useLight)
        {
            normalBuffer =
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
        }
        else normalBuffer = null

        if(shapeInfo.useTexture)
        {
            uvBuffer =
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
        }
        else uvBuffer = null


        /************* initiate Buffers *****************/


        if(shapeInfo.useFragColor && shapeInfo.useLight)
        {
            if(shapeInfo.vPosition == 8)  vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, "light_color_vert_tetra.glsl", myContext)
            else if(shapeInfo.vPosition == 13)  vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, "light_color_vert_star.glsl", myContext)
            else if(shapeInfo.vPosition == 15)  vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, "light_color_vert_arrow.glsl", myContext)
            else vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, "light_color_vert.glsl", myContext)

            if(shapeInfo.vNormal == 9) fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, "light_color_frag_tetra.glsl", myContext)
            else if(shapeInfo.vNormal == 14) fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, "light_color_frag_star.glsl", myContext)
            else if(shapeInfo.vNormal == 16) fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, "light_color_frag_arrow.glsl", myContext)
            else fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, "light_color_frag.glsl", myContext)
        }
        else if(shapeInfo.useTexture && shapeInfo.useLight)
        {
            vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, "light_tex_vert.glsl", myContext)
            fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, "light_tex_frag.glsl", myContext)
        }
        else if(shapeInfo.useFragColor)
        {
            vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, "color_vert.glsl", myContext)
            fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, "color_frag.glsl", myContext)
        }
        else if(shapeInfo.useLight)
        {
            vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, "light_uniform_color_vert.glsl", myContext)
            fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, "light_uniform_color_frag.glsl", myContext)
        }
        else if(shapeInfo.useTexture)
        {
            vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, "tex_vert.glsl", myContext)
            fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, "tex_frag.glsl", myContext)
        }
        else
        {
            if(shapeInfo.vPosition == 1) vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, "uniform_color_vert2.glsl", myContext)
            else if(shapeInfo.vPosition == 2) vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, "uniform_color_vert_flat.glsl", myContext)
            else vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, "uniform_color_vert.glsl", myContext)
            fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, "uniform_color_frag.glsl", myContext)
        }


        // create empty OpenGL ES Program
        mProgram = GLES30.glCreateProgram().also {

            // add the vertex shader to program
            GLES30.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES30.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES30.glLinkProgram(it)
        }

        // Add program to OpenGL ES environment
        GLES30.glUseProgram(mProgram)

        // enable a handle to the triangle vertices
        GLES30.glEnableVertexAttribArray(shapeInfo.vPosition)
        // Prepare the triangle coordinate data
        GLES30.glVertexAttribPointer (
            shapeInfo.vPosition,
            COORDS_PER_VERTEX,
            GLES30.GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer
        )

        if(shapeInfo.useFragColor)
        {
            GLES30.glEnableVertexAttribArray(shapeInfo.vColor)
            // Prepare the triangle coordinate data
            GLES30.glVertexAttribPointer (
                shapeInfo.vColor,
                COORDS_PER_VERTEX,
                GLES30.GL_FLOAT,
                false,
                vertexStride,
                colorBuffer
            )
        }
        else if(shapeInfo.useUniformColor)
        {
            mColorHandle = GLES30.glGetUniformLocation(mProgram, "fColor").also {

                // Set color for drawing the triangle
                GLES30.glUniform4fv(it, 1, vertexColors, 0)
            }
        }

        if(shapeInfo.useLight)
        {
            GLES30.glEnableVertexAttribArray(shapeInfo.vNormal)
            // Prepare the triangle coordinate data
            GLES30.glVertexAttribPointer (
                shapeInfo.vNormal,
                COORDS_PER_VERTEX,
                GLES30.GL_FLOAT,
                false,
                vertexStride,
                normalBuffer
            )

            mEyePosHandle = GLES30.glGetUniformLocation(mProgram, "eyePos").also {
                GLES30.glUniform3fv(it, 1, eyePos, 0)
            }
            //Log.d("mEyePosHandle", mEyePosHandle.toString())
            mLightAmbiHandle = GLES30.glGetUniformLocation(mProgram, "lightAmbi").also {
                GLES30.glUniform3fv(it, 1, lightAmbient, 0)
            }
            //Log.d("mLightAmbiHandle", mLightAmbiHandle.toString())

            mLightDirHandle = GLES30.glGetUniformLocation(mProgram, "lightDir").also {
                GLES30.glUniform3fv(it, 1, lightDir, 0)
            }
            //Log.d("mLightDirHandle", mLightDirHandle.toString())

            mLightSpecHandle = GLES30.glGetUniformLocation(mProgram, "lightSpec").also {
                GLES30.glUniform3fv(it, 1, lightSpecular, 0)
            }
            //Log.d("mLightSpecHandle", mLightSpecHandle.toString())

            mLightDiffHandle = GLES30.glGetUniformLocation(mProgram, "lightDiff").also {
                GLES30.glUniform3fv(it, 1, lightDiffuse, 0)
            }
            //Log.d("mLightDiffHandle", mLightDiffHandle.toString())


            mMatAmbiHandle = GLES30.glGetUniformLocation(mProgram, "matAmbi").also {
                GLES30.glUniform3fv(it, 1, matAmbient, 0)
            }
            //Log.d("mMatAmbiHandle", mMatAmbiHandle.toString())

            mMatSpecHandle = GLES30.glGetUniformLocation(mProgram, "matSpec").also {
                GLES30.glUniform3fv(it, 1, matSpecular, 0)
            }
            //Log.d("mMatSpecHandle", mMatSpecHandle.toString())

            mMatShHandle = GLES30.glGetUniformLocation(mProgram, "matSh").also {
                GLES30.glUniform1f(it, matShininess)
            }
            //Log.d("mMatShHandle", mMatShHandle.toString())
            GLES30.glUniform1f(GLES30.glGetUniformLocation(mProgram, "fogStart"), 5.0f)
            GLES30.glUniform1f(GLES30.glGetUniformLocation(mProgram, "fogEnd"), 10.0f)
            GLES30.glUniform3f(GLES30.glGetUniformLocation(mProgram, "fogColor"), 0.2f, 0.2f, 0.2f)

        }

        if(shapeInfo.useTexture)
        {
            GLES30.glEnableVertexAttribArray(shapeInfo.vTexCoord)
            GLES30.glVertexAttribPointer (
                shapeInfo.vTexCoord,
                2,
                GLES30.GL_FLOAT,
                false,
                0,
                uvBuffer
            )

            GLES30.glGenTextures(1, textureID, 0)

            GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureID[0])
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)

            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_MIRRORED_REPEAT)
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_MIRRORED_REPEAT)
            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, loadBitmap(shapeInfo.bitmapName, myContext), 0)
        }

        // get handle to shape's transformation matrix
        mvpMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix")
        mWorldMatHandle = GLES30.glGetUniformLocation(mProgram, "worldMat")
    }

    override fun draw(mvpMatrix: FloatArray, worldMat: FloatArray)
    {
        super.draw(mvpMatrix, worldMat)

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureID[0])

        GLES30.glUniform3fv(mEyePosHandle, 1, eyePos, 0)

        // Draw the triangle
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, drawOrder.size, GLES30.GL_UNSIGNED_SHORT, indexBuffer)
    }
}
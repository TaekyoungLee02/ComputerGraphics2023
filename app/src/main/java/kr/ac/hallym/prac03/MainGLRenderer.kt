package kr.ac.hallym.prac03

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import java.io.BufferedInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

const val COORDS_PER_VERTEX = 3

var eyePos = floatArrayOf(0.0f, 3.0f, 4.0f)
var lightDir = floatArrayOf(0.0f, 1.0f, 1.0f)
var lightAmbient = floatArrayOf(0.1f, 0.1f, 0.1f)
var lightDiffuse = floatArrayOf(1.0f, 1.0f, 1.0f)
var lightSpecular = floatArrayOf(0.5f, 0.5f, 0.5f)

class MainGLRenderer (val context:Context): GLSurfaceView.Renderer {

    private lateinit var enemies : MutableMap<Int, MyShape>
    private lateinit var modelMatrixes : MutableMap<Int, FloatArray>

    private lateinit var mTexGround: MyTexGround
    private lateinit var mTetra: MyShape
    private lateinit var mStar: MyShape
    private lateinit var mArrow: MyShape
    private lateinit var mTriangle: MyShape
    private lateinit var mLitHexa: MyShape
    private lateinit var mLitTexCube: MyShape
    private lateinit var gShape: MyShape

    private lateinit var myArcball: MyArcball
    private lateinit var myGesture: DrawGesture

    private var mvpMatrix = FloatArray(16)
    private var vpMatrix = FloatArray(16)
    private var flatvpMatrix = FloatArray(16)

    private var viewMatrix = FloatArray(16)
    private var flatviewMatrix = FloatArray(16)
    private var projectionMatrix = FloatArray(16)
    private var flatprojectionMatrix = FloatArray(16)
    private var modelMatrix1 = FloatArray(16)
    private var modelMatrix2 = FloatArray(16)
    private var modelMatrix3 = FloatArray(16)
    private var worldMatrix = FloatArray(16)

    private var startTime = SystemClock.uptimeMillis()
    private var clock : Int = 0
    private var gestureShape : Int = 0
    private var gestureDirection : Direction = Direction.NONE
    private var finalShape : Int = 0
    private var finalDirection : Direction = Direction.NONE
    var randX : Int = 0
    var randZ : Int = 0
    var enemyNumber : Int = 0

    private var shapeName : String = ""
    private var canDelete : Boolean = false
    private var gestureFinished : Boolean = false

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

        Matrix.setIdentityM(projectionMatrix, 0)
        Matrix.setIdentityM(flatprojectionMatrix, 0)
        Matrix.setIdentityM(mvpMatrix, 0)
        Matrix.setIdentityM(viewMatrix, 0)
        Matrix.setIdentityM(flatviewMatrix, 0)
        Matrix.setIdentityM(vpMatrix, 0)
        Matrix.setIdentityM(flatvpMatrix, 0)
        Matrix.setIdentityM(modelMatrix1, 0)
        Matrix.setIdentityM(modelMatrix2, 0)
        modelMatrix2[14] = -90.0f
        Matrix.setIdentityM(modelMatrix3, 0)
        modelMatrix3[14] = 90.0f
        Matrix.setIdentityM(worldMatrix, 0)

        GLES30.glClearColor(0.2f, 0.2f, 0.2f, 1.0f)

        mArrow = MyShape(MyArrow(), context)

        mTexGround = MyTexGround(context)
        mTetra = MyShape(MyLitTetra(), context)
        mStar = MyShape(MyStar(), context)
        mTriangle = MyShape(MyTriangle(), context)
        mLitHexa = MyShape(MyLitHexa(), context)
        mLitTexCube = MyShape(MyLitTexCube(), context)
        enemies = mutableMapOf()
        modelMatrixes = mutableMapOf()
        gShape = MyShape(MyArrow(), context)

        myArcball = MyArcball()
        myGesture = DrawGesture()

        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {

        GLES30.glViewport(0, 0, width, height)

        myArcball.resize(width, height)

        val ratio = width.toFloat() / height.toFloat()

        Matrix.perspectiveM(projectionMatrix, 0, 90f, ratio, 0.001f, 1000f)

        Matrix.setLookAtM(viewMatrix, 0, eyePos[0], eyePos[1], eyePos[2], 0f, 0f, 0f,
            0f, 1f, 0f)

        Matrix.multiplyMM(vpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        mTexGround.draw(vpMatrix, worldMatrix)

        val endTime = SystemClock.uptimeMillis()
        val deltaTime = 0.001f * (endTime - startTime).toInt()
        clock += (endTime - startTime).toInt()
        startTime = endTime


        if(clock > 3000)
        {
            Log.d("clock", clock.toString())

            val randomEnemy = (0..3).random()
            randX = (-1..1).random()
            randZ = (-20..-10).random()

            when(randomEnemy)
            {
                0 -> enemies[enemyNumber] = mArrow
                1 -> enemies[enemyNumber] = mTetra
                2 -> enemies[enemyNumber] = mLitTexCube
                3 -> enemies[enemyNumber] = mStar
            }

            Matrix.setIdentityM(modelMatrix2, 0)
            Matrix.translateM(modelMatrix2, 0, randX.toFloat(), 0.0f, randZ.toFloat())

            modelMatrixes[enemyNumber] = modelMatrix2.clone()

            clock = 0
            enemyNumber++
        }

        for((index, e) in enemies)
        {
//            modelMatrix1[12] = randX.toFloat()
            //modelMatrix1[14] = -2f
            //Matrix.setIdentityM(modelMatrix1, 0)
            //Matrix.translateM(modelMatrix1, 0, 0f, 0f, 0f)
            Matrix.translateM(modelMatrixes[index], 0, 0f, 0f, 0.05f)
            //Matrix.rotateM(modelMatrix2, 0, 45f, 0f, 0f, 1f)
            //Matrix.multiplyMM(modelMatrix1, 0, modelMatrix1, 0, myArcball.rotationMatrix, 0)
            //Matrix.multiplyMM(modelMatrix1, 0, modelMatrix1, 0, modelMatrix2, 0)
            Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, modelMatrixes[index], 0)
            mLitHexa.draw(mvpMatrix, modelMatrixes[index]!!)

            Matrix.translateM(modelMatrixes[index], 0, 0f, 1.5f, 0f)
            Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, modelMatrixes[index], 0)
            e.draw(mvpMatrix, modelMatrixes[index]!!)

            Matrix.translateM(modelMatrixes[index], 0, 0f, -1.5f, 0f)

            if(modelMatrixes[index]!![14] > 0f)
            {
                enemies.remove(index)
                life--
            }
        }

        //enemy.draw(mvpMatrix, modelMatrix1)

        Matrix.setIdentityM(mvpMatrix, 0)
        Matrix.setIdentityM(modelMatrix1, 0)
        Matrix.setIdentityM(modelMatrix2, 0)



//        if (modelMatrix1[14] > 10) modelMatrix3[14] = -80.0f
//        if (modelMatrix2[14] > 10) modelMatrix1[14] = -80.0f
//        if (modelMatrix3[14] > 10) modelMatrix2[14] = -80.0f

//        Log.d("1", modelMatrix1[14].toString())
//        Log.d("2", modelMatrix2[14].toString())
//        Log.d("3", modelMatrix3[14].toString())

//        Matrix.translateM(modelMatrix1, 0, 0f, 0f, deltaTime)
//        Matrix.translateM(modelMatrix2, 0, 0f, 0f, deltaTime)
//        Matrix.translateM(modelMatrix3, 0, 0f, 0f, deltaTime)

        //mGround.draw(mvpMatrix, modelMatrix1)






//        Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, modelMatrix1, 0)
//        mGround.draw(mvpMatrix, modelMatrix1)

//
//        Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, modelMatrix1, 0)
//        mGround.draw(mvpMatrix, modelMatrix1)
//        Matrix.setIdentityM(mvpMatrix, 0)

        //mTriangle.draw(flatvpMatrix, worldMatrix)


        Matrix.setIdentityM(modelMatrix1, 0)
        Matrix.translateM(modelMatrix1, 0, 0f, 0f, 2f)
        Matrix.multiplyMM(modelMatrix1, 0, modelMatrix1, 0, myArcball.rotationMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, modelMatrix1, 0)

        if(gestureFinished)
        {
            if(finalShape != gestureShape)
            {
                when (gestureShape)
                {
                    1 -> {
                        gShape = MyShape(MyArrow(), context)
                        shapeName = "MyArrow"
                    }
                    3 -> {
                        gShape = MyShape(MyLitTetra(), context)
                        shapeName = "MyLitTetra"
                    }
                    4 -> {
                        gShape = MyShape(MyLitTexCube(), context)
                        shapeName = "MyLitTexCube"
                    }
                    5 -> {
                        gShape = MyShape(MyStar(), context)
                        shapeName = "MyStar"
                    }
                }
            }
            canDelete = true
            gestureFinished = false
        }
        if(canDelete)
        {
            deleteFromList(shapeName)
            canDelete = false
            score++
        }

        gShape.draw(mvpMatrix, modelMatrix1)

    }

    fun onTouchEvent(event : MotionEvent): Boolean {

        val x = event.x.toInt()
        val y = event.y.toInt()

        when(event.action)
        {
            MotionEvent.ACTION_DOWN ->
            {
                myArcball.start(x, y)
                myGesture.start(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE ->
            {
                myArcball.end(x, y)
                myGesture.moving(event.x, event.y)
            }
            MotionEvent.ACTION_UP ->
            {
                gestureShape = myGesture.end()
                gestureDirection = myGesture.direction()
                Log.d("count", gestureShape.toString())
                gestureFinished = true
            }
        }

        return true
    }

    private fun deleteFromList(shapeName : String)
    {
        for((index, e) in enemies)
        {
            if(e.shapeName == shapeName) {
                enemies.remove(index)
                modelMatrixes.remove(index)
                break
            }
        }
    }
}

fun loadShader(type: Int, filename:String, myContext: Context): Int {

    // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
    // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
    return GLES30.glCreateShader(type).also { shader ->

        // add the source code to the shader and compile it
        val inputStream = myContext.assets.open(filename)
        val inputBuffer = ByteArray(inputStream.available())
        inputStream.read(inputBuffer)
        val shaderCode = String(inputBuffer)

        GLES30.glShaderSource(shader, shaderCode)
        GLES30.glCompileShader(shader)

        // log the compile error
        val compiled = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer()
        GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled)
        if (compiled.get(0) == 0)
        {
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled)
            if(compiled.get(0) > 1)
            {
                Log.e("Shader", "$type shader: " + GLES30.glGetShaderInfoLog(shader))
            }
            GLES30.glDeleteShader(shader)
            Log.e("Shader", "$type shader compile error")
        }
    }
}

fun loadBitmap(filename: String, myContext: Context): Bitmap {

    val manager = myContext.assets
    val inputStream = BufferedInputStream(manager.open(filename))
    val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
    return bitmap!!;
}


package kr.ac.hallym.prac03

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

var score : Int = 0
var life : Int = 5

class MainGLSurfaceView (context: Context) : GLSurfaceView(context) {

    private val mainRenderer : MainGLRenderer

    init {

        setEGLContextClientVersion(3)

        mainRenderer = MainGLRenderer(context)

        setRenderer(mainRenderer)

        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mainRenderer.onTouchEvent(event)
    }
}
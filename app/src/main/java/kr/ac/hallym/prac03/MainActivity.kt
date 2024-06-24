package kr.ac.hallym.prac03

import android.media.MediaCas
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.EventListener


class MainActivity : AppCompatActivity() {

    private lateinit var mainSurfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        mainSurfaceView = MainGLSurfaceView(this)
        setContentView(mainSurfaceView)
        supportActionBar?.hide()
    }
}
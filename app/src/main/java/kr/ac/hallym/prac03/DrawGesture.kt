package kr.ac.hallym.prac03

import android.util.Log
import kotlin.math.abs

enum class Direction
{
    NONE, UP, DOWN, LEFT, RIGHT
}

class DrawGesture {

    val RANGE = 80.0f
    private var width = 0
    private var height = 0
    private var lastPos = floatArrayOf(0.0f, 0.0f)
    private var count : Int = 0
    private var direction : Direction = Direction.NONE

    public fun resize(w: Int, h: Int)
    {
        width = w
        height = h
    }

    public fun start(x: Float, y: Float)
    {
        count = 0
        direction = Direction.NONE
        lastPos[0] = x
        lastPos[1] = y
    }

    public fun moving(x: Float, y: Float)
    {
        var currDirec = direction

        val dX = x - lastPos[0]
        val dY = y - lastPos[1]

        if(abs(dX) > RANGE || abs(dY) > RANGE)
        {
            currDirec = if(abs(dX) > abs(dY)) {
                if(dX > 0) Direction.LEFT
                else Direction.RIGHT
            } else {
                if(dY > 0) Direction.DOWN
                else Direction.UP
            }

            if(currDirec != direction)
            {
                count++
                direction = currDirec
                Log.d("count", count.toString())
                Log.d("Direction", direction.toString())

            }
        }

        lastPos[0] = x
        lastPos[1] = y

        //Log.d("Direction", direction.toString())
//        Log.d("dX", dX.toString())
//        Log.d("dY", dY.toString())
    }

    public fun end() : Int
    {
        return count
    }

    public fun direction() : Direction
    {
        return direction
    }
}
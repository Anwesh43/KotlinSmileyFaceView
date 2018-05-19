package com.example.smileyfaceview

/**
 * Created by anweshmishra on 20/05/18.
 */

import android.content.Context
import android.view.View
import android.view.MotionEvent
import android.graphics.*

class SmileyFaceView (ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State (var prevScale : Float = 0f, var dir : Float = 0f, var j : Int = 0) {

        val scales : Array<Float> = arrayOf(0f, 0f)

        fun update(stopcb : (Float) -> Unit) {
            scales[j] += dir * 0.1f
            if (Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += dir.toInt()
                if (j == scales.size || j == -1) {
                    j -= dir.toInt()
                    dir = 0f
                    prevScale = scales[j]
                    stopcb(prevScale)
                }
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class Animator (var view : View, var animated : Boolean = false) {

        fun animate(updatecb : () -> Unit) {
            if (animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class SmileyFace (var i : Int, val state : State = State()) {

        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val r : Float = Math.min(w, h) / 15
            canvas.save()
            canvas.translate(w/2 , h/2 + (h/2 + r) * (1 - state.scales[0]))
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#f1c40f")
            canvas.drawCircle(0f, 0f, r, paint)
            paint.color = Color.BLACK
            for (i in 0..1) {
                canvas.drawCircle((r/4) * (2 * i - 1), -r/2, r/10, paint)
            }
            paint.style = Paint.Style.STROKE
            val path : Path = Path()
            var x : Float = (r/2) * Math.sin(Math.PI/4).toFloat()
            var y : Float = (r/2) * state.scales[1] + (r/2) * Math.cos(Math.PI/4).toFloat()
            path.moveTo(x, y)
            for (i in 45..135) {
                x  = (r/2  * Math.sin(Math.PI/4).toFloat())
                y  = (r/2) * state.scales[1] + (r/2 * state.scales[1]) * Math.cos(Math.PI/4).toFloat()
                path.lineTo(x, y)
            }
            canvas.drawPath(path, paint)
            canvas.restore()
        }
    }

    data class Renderer(var view : SmileyFaceView) {

        private val animator : Animator = Animator(view)

        private val smileyFace : SmileyFace = SmileyFace(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            smileyFace.draw(canvas, paint)
            animator.animate {
                smileyFace.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            smileyFace.startUpdating {
                animator.stop()
            }
        }
    }
}
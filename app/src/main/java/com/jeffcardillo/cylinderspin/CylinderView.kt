package com.jeffcardillo.cylinderspin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class CylinderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var centerX: Float = 0f
    private var centerY: Float = 0f

    private var drawStep = 0f
    private var oldMod: Double = cos(0.0)

    private var cylinderHeight: Float = 0f
    private var halfCylinderWidth: Float = 0f

    private var colorTop: Int
    private var colorBody: Int

    private var direction = 1


    init {
        // load and apply xml attributes
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CylinderView)
        this.colorTop = attributes.getColor(R.styleable.CylinderView_colorTop, Color.CYAN)
        this.colorBody = attributes.getColor(R.styleable.CylinderView_colorBody, Color.BLUE)
        val spinDirectionDown = attributes.getBoolean(R.styleable.CylinderView_spinDirectionDown, true)

       when (spinDirectionDown) {
            true -> {
                drawStep = 0f
                direction = 1
            }
            false -> {
                drawStep = Math.PI.toFloat()/2
                direction = -1
            }
        }

        attributes.recycle()
    }

    private val bodyPaint = Paint().apply {
        isAntiAlias = true
        color = colorBody
        style = Paint.Style.FILL
    }

    private val topPaint = Paint().apply {
        isAntiAlias = true
        color =colorTop
        style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        centerX = w/2f
        centerY = h/2f

        cylinderHeight = w/2f
        halfCylinderWidth = w/2f

        Log.d("CylinderView", "w: $w h:$h cx:$centerX cy:$centerY halfWidth:$halfCylinderWidth height:$cylinderHeight")

        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.apply {
            // clear canvas
            drawColor(Color.TRANSPARENT)

            // move sin and cos along the x axis to get new y values
            drawStep += 0.05f * direction
            val cosMod = cos(drawStep.toDouble())
            val sinMod = sin(drawStep.toDouble())

            // detect when 180 degree rotation completed
            val isFront = (oldMod < cosMod)

            drawSide(this, isFront, sinMod, cosMod, bodyPaint)
            drawCylinderBody(this, cosMod, bodyPaint)
            drawSide(this, !isFront, sinMod, cosMod, topPaint)

            oldMod = cosMod
        }
    }

    private fun drawCylinderBody(canvas: Canvas?, cosMod: Double, paint: Paint) {
        canvas?.drawRect(
            centerX - halfCylinderWidth,
            (centerY - (cosMod * cylinderHeight)).toFloat(),
            centerX + halfCylinderWidth,
            (centerY + (cosMod * cylinderHeight)).toFloat(),
            paint)
    }

    private fun drawSide(canvas: Canvas?, isFront: Boolean, sinMod: Double, cosMod: Double, paint: Paint) {
        // spinOffset tracks the "rotation" of the cylinder
        val spinOffset = (cosMod * cylinderHeight)

        // circleSquash flattens or heightens the circle based on the sinMod
        val circleSquash = (halfCylinderWidth*sinMod)

        // topMod will change the sign of the vertical offset, moving the oval up or down
        val topMod = if (isFront) -1 else 1

        canvas?.drawOval(
            centerX - halfCylinderWidth,
            (centerY + topMod*spinOffset*direction - circleSquash).toFloat(),
            centerX + halfCylinderWidth,
            (centerY + topMod*spinOffset*direction + circleSquash).toFloat(),
            paint
        )
    }
}
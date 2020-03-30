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
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CylinderView)
        colorTop = attributes.getColor(R.styleable.CylinderView_colorTop, Color.CYAN)
        colorBody = attributes.getColor(R.styleable.CylinderView_colorBody, Color.BLUE)
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


    private val body = Paint().apply {
        isAntiAlias = true
        color = colorBody
        style = Paint.Style.FILL
    }

    private val top = Paint().apply {
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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawStep += 0.05f * direction

        // clear canvas
        canvas?.drawColor(Color.TRANSPARENT)

        val cosMod = cos(drawStep.toDouble())
        val sinMod = sin(drawStep.toDouble())

        if (oldMod < cosMod) {
            drawSide1(canvas, sinMod, cosMod, body)
            drawCylinderBody(canvas, cosMod, body)
            drawSide2(canvas, sinMod, cosMod, top)
        } else {
            drawSide2(canvas, sinMod, cosMod, body)
            drawCylinderBody(canvas, cosMod, body)
            drawSide1(canvas, sinMod, cosMod,top)
        }

        oldMod = cosMod
    }

    private fun drawCylinderBody(canvas: Canvas?, cosMod: Double, paint: Paint) {
        canvas?.drawRect(
            centerX - halfCylinderWidth,
            (centerY - (cosMod * cylinderHeight)).toFloat(),
            centerX + halfCylinderWidth,
            (centerY + (cosMod * cylinderHeight)).toFloat(),
            paint)
    }

    private fun drawSide1(canvas: Canvas?, sinMod: Double, cosMod: Double, paint: Paint) {
        val spinOffset = (cosMod * cylinderHeight)
        val circleSquash = (halfCylinderWidth*sinMod)
        canvas?.drawOval(
            centerX - halfCylinderWidth,
            (centerY - spinOffset*direction - circleSquash).toFloat(),
            centerX + halfCylinderWidth,
            (centerY - spinOffset*direction + circleSquash).toFloat(),
            paint
        )
    }

    private fun drawSide2(canvas: Canvas?, sinMod: Double, cosMod: Double, paint: Paint) {
        val spinOffset = (cosMod * cylinderHeight)
        val circleSquash = (halfCylinderWidth*sinMod)

        canvas?.drawOval(
            centerX - halfCylinderWidth,
            (centerY + spinOffset*direction - circleSquash).toFloat(),
            centerX + halfCylinderWidth,
            (centerY + spinOffset*direction + circleSquash).toFloat(),
            paint
        )
    }
}
package com.example.myfundkt.ui.lineChart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * We need to modify these variables and functions manually:
 *
 *
 * levelFormat
 * getMaxData()
 * getMinData()
 * ...
 */
class LineChart @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    mContext, attrs, defStyleAttr
) {
    class Data<T> {
        // for xAxis
        constructor(v: T) {
            value = v
            bottomLabel = ""
            leftLabel = ""
        }

        //for fund
        constructor(l: T, r: Float?, bottomLabel: String) {
            rightvalue = r
            value = l
            this.bottomLabel = bottomLabel
            leftLabel = String.format(levelFormat, value)
            rightlabel = String.format(rightFormat, rightvalue)
        }

        // for data
        constructor(v: T, bottomLabel: String) {
            value = v
            this.bottomLabel = bottomLabel
            leftLabel = String.format(levelFormat, value)
        }

        // for data expansion
        constructor(v: T, bottomLabel: String, leftLabel: String) {
            value = v
            this.bottomLabel = bottomLabel
            this.leftLabel = leftLabel
        }

        var value: T
        var rightvalue: Float? = null
        var bottomLabel: String
        var leftLabel: String
        var rightlabel: String? = null
    }

    private var mSelfWidth = 0f
    private var mSelfHeight = 0f
    private val mPaint: Paint
    private val mBackgroundPaint: Paint
    private val mTipPaint: Paint
    private var xAxisBasisData: List<Data<String>>? = ArrayList()
    private var mData: List<Data<Float>> = ArrayList()
    private var midStart = 0f
    private val mPoints: MutableList<PointF> = ArrayList()
    private  var leftTextSpace = DEFAULT_LEFT_TEXT_SPACE
    private var horizontalSpaceLeft = 150f
    private var horizontalSpaceRight = 150f
    private var verticalSpaceTop = 75f
    private var verticalSpaceBottom = 100f
    private var bottomVerticalLineHeight = 20f
    private var proportionWidth = 0f
    private var movingPointRadiusOuter = DEFAULT_MOVING_POINT_RADIUS_OUTER
    private var movingPointRadiusInner = DEFAULT_MOVING_POINT_RADIUS_INNER
    private var bottomLineWidth = DEFAULT_BOTTOM_LINE_WIDTH
    private var levelLineWidth = DEFAULT_LEVEL_LINE_WIDTH
    private var bottomVerticalLineWidth = DEFAULT_BOTTOM_VERTICAL_LINE_WIDTH
    private var brokenLineWidth = DEFAULT_BROKEN_LINE_WIDTH
    private var movingLineWidth = DEFAULT_MOVING_LINE_WIDTH
    private var textSize = DEFAULT_TEXT_SIZE
    private var levelDashDip = DEFAULT_DASH_DIP2
    private var movingLineDashDip = DEFAULT_DASH_DIP4
    private var textPadding = DEFAULT_TEXT_PADDING
    private var linePath: Path
    private var backgroundPath: Path
    private var isDrawMoveLine = false
    private var moveX = 0f
    private var moveY = 0f
    private var moveIndex = 0
    private var levels = DEFAULT_LEVEL_NUMBER
    @Throws(Exception::class)
    fun setXAxisBasisData(xAxisBasisData: List<Data<String>>?) {
        if (xAxisBasisData == null || xAxisBasisData.size < 2) {
            throw Exception("xAxisBasisData not valid")
        }
        this.xAxisBasisData = null
        this.xAxisBasisData = xAxisBasisData
        invalidate()
    }

    @Throws(Exception::class)
    fun setData(data: List<Data<Float>>) {
        if (xAxisBasisData == null) {
            throw Exception("xAxisBasisData is null")
        }
        mData = data
        invalidate()
    }

    @Throws(Exception::class)
    fun setMidStart(f: Float) {
        if (xAxisBasisData == null) {
            throw Exception("xAxisBasisData is null")
        }
        midStart = f
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mSelfWidth = w.toFloat()
        mSelfHeight = h.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        init()
        initPoint()
        drawBottomText(canvas)
        drawBottomLine(canvas)
        drawLevelLine(canvas)
        drawLevelText(canvas)
        drawRightText(canvas)
        drawBackground(canvas)
        drawBrokenLine(canvas)
        drawMovingLine(canvas)
    }

    private fun init() {
        proportionWidth =
            (mSelfWidth - horizontalSpaceLeft - horizontalSpaceRight) / (xAxisBasisData!!.size - 1)
        Log.d(TAG, "init:xAxisBasisData.size()  " + xAxisBasisData!!.size)
        Log.d(TAG, "init:proportionWidth $proportionWidth")
    }

    private fun initPoint() {
        val brokenLineHeight = mSelfHeight - verticalSpaceTop - verticalSpaceBottom
        val proportionHeight = brokenLineHeight / (maxData - minData)
        var circleCenterX = horizontalSpaceLeft
        var circleCenterY: Float
        mPoints.clear()
        for (i in mData.indices) {
            val currentProportionHeight = (mData[i].value - minData) * proportionHeight
            circleCenterY = brokenLineHeight - currentProportionHeight + verticalSpaceTop
            mPoints.add(PointF(circleCenterX, circleCenterY))
            circleCenterX += proportionWidth
        }
    }

    private fun drawBottomText(canvas: Canvas) {
        val currentTextX: Float = horizontalSpaceLeft
        val currentTextY: Float = mSelfHeight - LineChartUtils.sp2px(mContext, textSize)
        mPaint.reset()
        mPaint.textSize = LineChartUtils.sp2px(mContext, textSize)
        mPaint.color = Color.DKGRAY
        mPaint.textAlign = Paint.Align.LEFT
        canvas.drawText("09:30", currentTextX, currentTextY, mPaint)
        mPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("11:30/13:00", mSelfWidth / 2, currentTextY, mPaint)
        mPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText("15:00", mSelfWidth - horizontalSpaceRight, currentTextY, mPaint)
    }

    private fun drawBottomLine(canvas: Canvas) {
        val bottomLineStartX = horizontalSpaceLeft
        val bottomLineStopX = mSelfWidth - horizontalSpaceRight
        val bottomLineStartY = mSelfHeight - verticalSpaceBottom
        mPaint.color = BOTTOM_LINE_COLOR
        mPaint.strokeWidth = bottomLineWidth
        canvas.drawLine(
            bottomLineStartX,
            bottomLineStartY,
            bottomLineStopX,
            bottomLineStartY,
            mPaint
        )
        var verticalLineStartX = horizontalSpaceLeft
        val verticalLineStartY = mSelfHeight - verticalSpaceBottom
        var verticalLineStopX = verticalLineStartX
        val verticalLineStopY = verticalLineStartY + bottomVerticalLineHeight
        for (i in xAxisBasisData!!.indices) {
            if (i != 0 && i != xAxisBasisData!!.size - 1) {
                verticalLineStartX += proportionWidth
                verticalLineStopX = verticalLineStartX
                continue
            }
            mPaint.strokeWidth = bottomVerticalLineWidth
            mPaint.color = BOTTOM_VERTICAL_LINE_COLOR
            canvas.drawLine(
                verticalLineStartX,
                verticalLineStartY,
                verticalLineStopX,
                verticalLineStopY,
                mPaint
            )
            verticalLineStartX = verticalLineStartX + proportionWidth
            verticalLineStopX = verticalLineStartX
        }
    }

    private fun drawLevelLine(canvas: Canvas) {
        if (levels <= 0) return
        val brokenLineHeight = mSelfHeight - verticalSpaceTop - verticalSpaceBottom
        val proportionHeight = brokenLineHeight / (maxData - minData)
        val oneLevelValueHeight = (maxData - minData) / levels * proportionHeight
        for (i in 0..levels) {
            if (i == 0) continue
            val lineStartX = horizontalSpaceLeft
            val lineStopX = mSelfWidth - horizontalSpaceRight
            val lineStartY = mSelfHeight - verticalSpaceBottom - i * oneLevelValueHeight
            mPaint.style = Paint.Style.STROKE
            mPaint.color = LEVEL_LINE_COLOR
            mPaint.strokeWidth = levelLineWidth
            val dash = LineChartUtils.dp2px(mContext, levelDashDip)
            mPaint.pathEffect = DashPathEffect(floatArrayOf(dash, dash, dash, dash), 0F)
            linePath.reset()
            linePath.moveTo(lineStartX, lineStartY)
            linePath.lineTo(lineStopX, lineStartY)
            canvas.drawPath(linePath, mPaint)
        }
    }

    private fun drawLevelText(canvas: Canvas) {
        if (levels < 0) return
        val brokenLineHeight = mSelfHeight - verticalSpaceTop - verticalSpaceBottom
        val proportionHeight = brokenLineHeight / (maxData - minData)
        val oneLevelValueHeight = (maxData - minData) / levels * proportionHeight
        val rect = Rect()
        val text = String.format(levelFormat, maxData)
        mPaint.reset()
        mPaint.getTextBounds(text, 0, text.length, rect)
        val fontWidth = rect.width().toFloat()
        val fontHeight = rect.height().toFloat()
        mPaint.color = FONT_COLOR
        mPaint.textAlign = Paint.Align.RIGHT
        mPaint.textSize = LineChartUtils.dp2px(mContext, textSize)
        for (i in 0..levels) {
            val textStartX = horizontalSpaceLeft - leftTextSpace
            val textStartY =
                mSelfHeight - verticalSpaceBottom - i * oneLevelValueHeight + fontHeight
            val text2 = String.format(levelFormat, minData + (maxData - minData) / levels * i) + "%"
            if (text2.contains("-")) {
                mPaint.color = -0xff77f2 //green
            } else {
                mPaint.color = Color.RED
            }
            canvas.drawText(text2, textStartX, textStartY, mPaint)
        }
    }

    private fun drawRightText(canvas: Canvas) {
        if (levels < 0) return
        val brokenLineHeight = mSelfHeight - verticalSpaceTop - verticalSpaceBottom
        val proportionHeight = brokenLineHeight / (maxData - minData)
        val oneLevelValueHeight = (maxData - minData) / levels * proportionHeight
        val rect = Rect()
        val text = String.format(levelFormat, percentMaxData)
        mPaint.reset()
        mPaint.getTextBounds(text, 0, text.length, rect)
        val fontWidth = rect.width().toFloat()
        val fontHeight = rect.height().toFloat()
        mPaint.color = Color.DKGRAY
        mPaint.textAlign = Paint.Align.RIGHT
        mPaint.textSize = LineChartUtils.dp2px(mContext, textSize)
        Log.d(TAG, "getPercentMinData: $percentMinData")
        //        Math.min(min, mData.get(i).rightvalue)
        //中间层
        val m = (levels + 1) / 2
        for (i in 0..levels) {
            val textStartX = mSelfWidth - leftTextSpace
            val textStartY =
                mSelfHeight - verticalSpaceBottom - i * oneLevelValueHeight + fontHeight
            if (i == m) {
                val text2 = String.format(rightFormat, midStart)
                canvas.drawText(text2, textStartX, textStartY, mPaint)
            } else {
                val percent = minData + (maxData - minData) / levels * i
                val text2 = String.format(rightFormat, (percent / 100 + 1) * midStart)
                canvas.drawText(text2, textStartX, textStartY, mPaint)
            }
        }
    }

    private fun drawBrokenLine(canvas: Canvas) {
        linePath.reset()
        for (i in mPoints.indices) {
            if (i == 0) {
                linePath.moveTo(mPoints[i].x, mPoints[i].y)
            } else {
                linePath.lineTo(mPoints[i].x, mPoints[i].y)
            }
        }
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = brokenLineWidth
        mPaint.color = BROKEN_LINE_COLOR
        canvas.drawPath(linePath, mPaint)
    }

    private fun drawBackground(canvas: Canvas) {
        if (mPoints.isEmpty() || mPoints.size == 1) {
            return
        }
        val brokenLineHeight = mSelfHeight - verticalSpaceTop - verticalSpaceBottom
        val proportionHeight = brokenLineHeight / (maxData - minData)
        backgroundPath.reset()
        val bottomHeight = verticalSpaceBottom
        backgroundPath.moveTo(horizontalSpaceLeft, mSelfHeight - bottomHeight)
        for (i in mPoints.indices) {
            backgroundPath.lineTo(mPoints[i].x, mPoints[i].y)
        }
        val bgPathEndX = mPoints[mPoints.size - 1].x
        backgroundPath.lineTo(bgPathEndX, mSelfHeight - bottomHeight)
        backgroundPath.close()
        mBackgroundPaint.style = Paint.Style.FILL
        val shaderStartX = horizontalSpaceLeft + proportionWidth / 2
        val shaderStartY = mSelfHeight - bottomHeight - (maxData - minData) * proportionHeight
        val shaderStopY = mSelfHeight - bottomHeight
        val shader: Shader = LinearGradient(
            shaderStartX,
            shaderStartY,
            shaderStartX,
            shaderStopY,
            LINEAR_GRADIENT_START_COLOR,
            LINEAR_GRADIENT_STOP_COLOR,
            Shader.TileMode.CLAMP
        )
        mBackgroundPaint.shader = shader
        canvas.drawPath(backgroundPath, mBackgroundPaint)
    }

    private fun drawMovingLine(canvas: Canvas) {
        if (isDrawMoveLine) {
            if (moveIndex == -1 || moveIndex >= mData.size) return
            mPaint.style = Paint.Style.STROKE
            mPaint.strokeWidth = movingLineWidth
            val dash = LineChartUtils.dp2px(mContext, movingLineDashDip)
            mPaint.color = MOVING_LINE_COLOR
            mPaint.pathEffect = DashPathEffect(floatArrayOf(dash, dash, dash, dash), 0F)
            val lineStartX = horizontalSpaceLeft
            val lineStopX = mSelfWidth - horizontalSpaceRight
            linePath.reset()
            linePath.moveTo(lineStartX, moveY)
            linePath.lineTo(lineStopX, moveY)
            canvas.drawPath(linePath, mPaint)
            val lineStartY = verticalSpaceTop
            val lineStopY = mSelfHeight - verticalSpaceBottom
            linePath.reset()
            linePath.moveTo(moveX, lineStartY)
            linePath.lineTo(moveX, lineStopY)
            canvas.drawPath(linePath, mPaint)
            mPaint.reset()
            mPaint.color = POINT_OUTER_COLOR
            mPaint.style = Paint.Style.FILL
            canvas.drawCircle(moveX, moveY, movingPointRadiusOuter, mPaint)
            mPaint.color = POINT_INNER_COLOR
            mPaint.style = Paint.Style.FILL
            canvas.drawCircle(moveX, moveY, movingPointRadiusInner, mPaint)
            run {

                // bottom
                val rect = Rect()
                mTipPaint.reset()
                mTipPaint.textSize = LineChartUtils.sp2px(mContext, textSize)
                mTipPaint.style = Paint.Style.FILL
                mTipPaint.color = BOTTOM_TIP_FRAME_COLOR
                mTipPaint.getTextBounds(
                    xAxisBasisData!![0].value,
                    0,
                    xAxisBasisData!![0].value.length,
                    rect
                )
                val fontWidth = rect.width().toFloat()
                val fontHeight = rect.height().toFloat()
                val left = moveX - fontWidth / 2 - textPadding
                val right = moveX + fontWidth / 2 + textPadding
                val top = mSelfHeight - verticalSpaceBottom + bottomVerticalLineHeight / 2
                val bottom = top + textPadding * 2 + fontHeight
                val rectF = RectF(left, top, right, bottom)
                canvas.drawRoundRect(rectF, 7f, 7f, mTipPaint)
                mTipPaint.strokeWidth = 8f
                mTipPaint.textAlign = Paint.Align.CENTER
                mTipPaint.color = Color.WHITE
                val text = mData[moveIndex].bottomLabel
                val fontMetrics = mTipPaint.fontMetrics
                val distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
                val baseline = rectF.centerY() + distance
                canvas.drawText(text, rectF.centerX(), baseline, mTipPaint)
            }
            run {

                // left
                val rect = Rect()
                mTipPaint.reset()
                mTipPaint.textSize = LineChartUtils.sp2px(mContext, textSize)
                mTipPaint.style = Paint.Style.FILL
                mTipPaint.color = LEFT_TIP_FRAME_COLOR
                mTipPaint.getTextBounds(maxData.toString(), 0, maxData.toString().length, rect)
                val fontWidth = rect.width().toFloat()
                val fontHeight = rect.height().toFloat()
                val left = horizontalSpaceLeft - leftTextSpace - fontWidth - textPadding * 4
                val right = horizontalSpaceLeft - leftTextSpace
                val top = moveY - fontHeight / 2 - textPadding
                val bottom = moveY + fontHeight / 2 + textPadding
                val rectF = RectF(left, top, right, bottom)
                canvas.drawRoundRect(rectF, 7f, 7f, mTipPaint)
                mTipPaint.strokeWidth = 8f
                mTipPaint.textAlign = Paint.Align.CENTER
                mTipPaint.color = Color.WHITE
                val text = mData[moveIndex].leftLabel
                val fontMetrics = mTipPaint.fontMetrics
                val distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
                val baseline = rectF.centerY() + distance
                canvas.drawText("$text%", rectF.centerX(), baseline, mTipPaint)
            }
            run {

                // right
                val rect = Rect()
                mTipPaint.reset()
                mTipPaint.textSize = LineChartUtils.sp2px(mContext, textSize)
                mTipPaint.style = Paint.Style.FILL
                mTipPaint.color = LEFT_TIP_FRAME_COLOR
                mTipPaint.getTextBounds(maxData.toString(), 0, maxData.toString().length, rect)
                val fontWidth = rect.width().toFloat()
                val fontHeight = rect.height().toFloat()
                val left =
                    mSelfWidth - horizontalSpaceRight - leftTextSpace - fontWidth - textPadding * 4
                val right = mSelfWidth - horizontalSpaceRight
                val top = moveY - fontHeight / 2 - textPadding
                val bottom = moveY + fontHeight / 2 + textPadding
                val rectF = RectF(left, top, right, bottom)
                canvas.drawRoundRect(rectF, 7f, 7f, mTipPaint)
                mTipPaint.strokeWidth = 8f
                mTipPaint.textAlign = Paint.Align.CENTER
                mTipPaint.color = Color.WHITE
                val text = mData[moveIndex].rightlabel
                val fontMetrics = mTipPaint.fontMetrics
                val distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
                val baseline = rectF.centerY() + distance
                canvas.drawText(text!!, rectF.centerX(), baseline, mTipPaint)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> isDrawMoveLine = true
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isDrawMoveLine = false
                moveIndex = -1
            }
        }
        countRoundPoint(x)
        invalidate()
        return true
    }

    private val maxData: Float
         get() = if (minData_ == maxData_) {
            minData_ + DEFAULT_VALUE_GAP
        } else maxData_
    private val minData: Float
         get() = if (minData_ == maxData_) {
            minData_ - DEFAULT_VALUE_GAP
        } else minData_
    private val percentMaxData: Float
         get() = if (minData_percent == maxData_percent) {
            minData_percent + DEFAULT_VALUE_GAP
        } else maxData_percent
    private val percentMinData: Float
         get() = if (minData_percent == maxData_percent) {
            minData_percent - DEFAULT_VALUE_GAP
        } else minData_percent

    // for 1.x 2.x 3.x 4.x, set max to 4+1
    private val maxData_: Float
         get() {
            var max = Int.MIN_VALUE.toFloat()
            for (i in mData.indices) {
                max = max.coerceAtLeast(mData[i].value)
            }
            return if (max == Int.MIN_VALUE.toFloat()) {
                DEFAULT_MAX_VALUE
            } else ceil(max.toDouble()).toFloat()
        }

    // for 1.x 2.x 3.x 4.x, set min to 1
    private val minData_: Float
         get() {
            var min = Int.MAX_VALUE.toFloat()
            for (i in mData.indices) {
                min = min.coerceAtMost(mData[i].value)
            }
            return if (min == Int.MAX_VALUE.toFloat()) {
                DEFAULT_MIN_VALUE
            } else floor(min.toDouble()).toFloat()
        }

    // for 1.x 2.x 3.x 4.x, set max to 4+1
    private val maxData_percent: Float
         get() {
            var max = Float.MIN_VALUE
            for (i in mData.indices) {
                max = max.coerceAtLeast(mData[i].rightvalue!!)
            }
            return if (max == Float.MIN_VALUE) {
                DEFAULT_MAX_VALUE
            } else ceil(max.toDouble()).toFloat()
        }

    // for 1.x 2.x 3.x 4.x, set min to 1
    private val minData_percent: Float
         get() {
            var min = Float.MAX_VALUE
            for (i in mData.indices) {
                min = min.coerceAtMost(mData[i].rightvalue!!)
            }
            return if (min == Float.MAX_VALUE) {
                DEFAULT_MIN_VALUE
            } else floor(min.toDouble()).toFloat()
        }

    private fun countRoundPoint(x: Float) {
        var location = ((x - horizontalSpaceLeft) / proportionWidth).roundToInt()
        if (location < 0) location = 0
        if (mData.isEmpty()) {
            moveIndex = -1
            return
        }
        if (mPoints.isEmpty()) {
            moveIndex = -1
            return
        }
        if (location >= mData.size) location = mData.size - 1
        if (location >= mPoints.size) {
            moveIndex = -1
            return
        }
        moveIndex = location
        moveX = mPoints[location].x
        moveY = mPoints[location].y
    }

    companion object {
        private const val TAG = "LineChart"
        private const val FONT_COLOR = -0x373738
        private const val LEVEL_LINE_COLOR = -0x282829
        private const val BOTTOM_LINE_COLOR = -0x363637
        private const val BOTTOM_VERTICAL_LINE_COLOR = -0x363637
        private const val MOVING_LINE_COLOR = -0xe88801
        private const val BROKEN_LINE_COLOR = -0xe88801
        private const val LINEAR_GRADIENT_START_COLOR = 0x2D266CDE
        private const val LINEAR_GRADIENT_STOP_COLOR = 0x00FFFFFF
        private const val POINT_INNER_COLOR = -0xe88801
        private const val POINT_OUTER_COLOR = -0x1
        private const val BOTTOM_TIP_FRAME_COLOR = -0xe88801
        private const val LEFT_TIP_FRAME_COLOR = -0xe88801
        private const val DEFAULT_LEVEL_NUMBER = 5
        private const val DEFAULT_MAX_VALUE = 500f
        private const val DEFAULT_MIN_VALUE = 200f
        private const val DEFAULT_VALUE_GAP = 30f
        private const val DEFAULT_TEXT_SIZE = 12f // sp
        private const val DEFAULT_LEVEL_LINE_WIDTH = 4f
        private const val DEFAULT_BOTTOM_LINE_WIDTH = 4f
        private const val DEFAULT_BROKEN_LINE_WIDTH = 5f
        private const val DEFAULT_BOTTOM_VERTICAL_LINE_WIDTH = 2f
        private const val DEFAULT_MOVING_LINE_WIDTH = 3f
        private const val DEFAULT_MOVING_POINT_RADIUS_INNER = 16f
        private const val DEFAULT_MOVING_POINT_RADIUS_OUTER = DEFAULT_MOVING_POINT_RADIUS_INNER + 3
        private const val DEFAULT_LEFT_TEXT_SPACE = 5f
        private const val DEFAULT_RIGHT_TEXT_SPACE = 5f
        private const val DEFAULT_TEXT_PADDING = 18f
        private const val DEFAULT_DASH_DIP1 = 1f
        private const val DEFAULT_DASH_DIP2 = 2f
        private const val DEFAULT_DASH_DIP3 = 3f
        private const val DEFAULT_DASH_DIP4 = 4f
        var levelFormat = "%.2f"
        var rightFormat = "%.4f"
    }

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTipPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        linePath = Path()
        backgroundPath = Path()
    }
}
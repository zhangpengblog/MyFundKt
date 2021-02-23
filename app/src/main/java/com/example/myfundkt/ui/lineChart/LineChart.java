package com.example.myfundkt.ui.lineChart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * We need to modify these variables and functions manually:
 *
 *      levelFormat
 *      getMaxData()
 *      getMinData()
 *      ...
 *
 */
public class LineChart extends View {
    private static final String TAG = "LineChart";
    public static class Data<T> {
        // for xAxis
        public Data(T v) {
            value = v;
            bottomLabel = "";
            leftLabel = "";
        }

        //for fund
        public Data(T l,T r, String bottomLabel){
            rightvalue =r;
            value = l;
            this.bottomLabel = bottomLabel;
            leftLabel = String.format(levelFormat, value);
            rightlabel = String.format(rightFormat,rightvalue);
        }

        // for data
        public Data(T v, String bottomLabel) {
            value = v;
            this.bottomLabel = bottomLabel;
            leftLabel = String.format(levelFormat, value);
        }

        // for data expansion
        public Data(T v, String bottomLabel, String leftLabel) {
            value = v;
            this.bottomLabel = bottomLabel;
            this.leftLabel = leftLabel;
        }


        public T value;
        public T rightvalue;
        public String bottomLabel;
        public String leftLabel;
        public String rightlabel;
    }

    private static final int FONT_COLOR = 0xFFC8C8C8;
    private static final int LEVEL_LINE_COLOR = 0xFFD7D7D7;
    private static final int BOTTOM_LINE_COLOR = 0xFFC9C9C9;
    private static final int BOTTOM_VERTICAL_LINE_COLOR = 0xFFC9C9C9;
    private static final int MOVING_LINE_COLOR = 0xFF1777FF;
    private static final int BROKEN_LINE_COLOR = 0xFF1777FF;
    private static final int LINEAR_GRADIENT_START_COLOR = 0x2D266CDE;
    private static final int LINEAR_GRADIENT_STOP_COLOR = 0x00FFFFFF;
    private static final int POINT_INNER_COLOR = 0xFF1777FF;
    private static final int POINT_OUTER_COLOR = 0xFFFFFFFF;
    private static final int BOTTOM_TIP_FRAME_COLOR = 0xFF1777FF;
    private static final int LEFT_TIP_FRAME_COLOR = 0xFF1777FF;

    private static final int DEFAULT_LEVEL_NUMBER = 5;
    private static final float DEFAULT_MAX_VALUE = 500f;
    private static final float DEFAULT_MIN_VALUE = 200f;
    private static final float DEFAULT_VALUE_GAP = 30f;
    private static final float DEFAULT_TEXT_SIZE = 12f; // sp
    private static final float DEFAULT_LEVEL_LINE_WIDTH = 4;
    private static final float DEFAULT_BOTTOM_LINE_WIDTH = 4;
    private static final float DEFAULT_BROKEN_LINE_WIDTH = 5;
    private static final float DEFAULT_BOTTOM_VERTICAL_LINE_WIDTH = 2;
    private static final float DEFAULT_MOVING_LINE_WIDTH = 3;
    private static final float DEFAULT_MOVING_POINT_RADIUS_INNER = 16;
    private static final float DEFAULT_MOVING_POINT_RADIUS_OUTER = DEFAULT_MOVING_POINT_RADIUS_INNER + 3;
    private static final float DEFAULT_LEFT_TEXT_SPACE = 5;
    private static final float DEFAULT_RIGHT_TEXT_SPACE = 5;
    private static final float DEFAULT_TEXT_PADDING = 18;
    private static final float DEFAULT_DASH_DIP1 = 1;
    private static final float DEFAULT_DASH_DIP2 = 2;
    private static final float DEFAULT_DASH_DIP3 = 3;
    private static final float DEFAULT_DASH_DIP4 = 4;

    private float mSelfWidth;
    private float mSelfHeight;

    private Paint mPaint;
    private Paint mBackgroundPaint;
    private Paint mTipPaint;
    private List<Data<String>> xAxisBasisData = new ArrayList<>();
    private List<Data<Float>> mData = new ArrayList<>();
    private Float midStart = 0f ;
    private List<PointF> mPoints = new ArrayList<>();

    float leftTextSpace = DEFAULT_LEFT_TEXT_SPACE;
    float horizontalSpaceLeft = 150f;
    float horizontalSpaceRight = 150f;
    float verticalSpaceTop = 75f;
    float verticalSpaceBottom = 100f;
    float bottomVerticalLineHeight = 20f;
    float proportionWidth;
    float movingPointRadiusOuter = DEFAULT_MOVING_POINT_RADIUS_OUTER;
    float movingPointRadiusInner = DEFAULT_MOVING_POINT_RADIUS_INNER;
    float bottomLineWidth = DEFAULT_BOTTOM_LINE_WIDTH;
    float levelLineWidth = DEFAULT_LEVEL_LINE_WIDTH;
    float bottomVerticalLineWidth = DEFAULT_BOTTOM_VERTICAL_LINE_WIDTH;
    float brokenLineWidth = DEFAULT_BROKEN_LINE_WIDTH;
    float movingLineWidth = DEFAULT_MOVING_LINE_WIDTH;
    float textSize = DEFAULT_TEXT_SIZE;
    float levelDashDip = DEFAULT_DASH_DIP2;
    float movingLineDashDip = DEFAULT_DASH_DIP4;
    float textPadding = DEFAULT_TEXT_PADDING;
    Path linePath;
    Path backgroundPath;
    boolean isDrawMoveLine;
    float moveX;
    float moveY;
    int moveIndex;
    int levels = DEFAULT_LEVEL_NUMBER;
    static String levelFormat = "%.2f";
    static String rightFormat = "%.4f";

    private Context mContext;

    public LineChart(Context context) {
        this(context, null);
    }

    public LineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mTipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        linePath = new Path();
        backgroundPath = new Path();
    }

    public void setXAxisBasisData(List<Data<String>> xAxisBasisData) throws Exception {
        if (xAxisBasisData == null || xAxisBasisData.size() < 2) {
            throw new Exception("xAxisBasisData not valid");
        }
        this.xAxisBasisData = null;
        this.xAxisBasisData = xAxisBasisData;
        invalidate();
    }

    public void setData(List<Data<Float>> data) throws Exception {
        if (xAxisBasisData == null) {
            throw new Exception("xAxisBasisData is null");
        }
        this.mData = data;
        invalidate();
    }
    public void setMidStart(Float f) throws Exception {
        if (xAxisBasisData == null) {
            throw new Exception("xAxisBasisData is null");
        }
        this.midStart = f;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSelfWidth = w;
        mSelfHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        initPoint();
        drawBottomText(canvas);
        drawBottomLine(canvas);
        drawLevelLine(canvas);
        drawLevelText(canvas);
        drawRightText(canvas);
        drawBackground(canvas);
        drawBrokenLine(canvas);
        drawMovingLine(canvas);
    }

    private void init() {
        proportionWidth = (mSelfWidth - horizontalSpaceLeft - horizontalSpaceRight) / (xAxisBasisData.size() - 1);
        Log.d(TAG, "init:xAxisBasisData.size()  "+xAxisBasisData.size() );
        Log.d(TAG, "init:proportionWidth "+proportionWidth);
    }

    private void initPoint() {
        float brokenLineHeight = mSelfHeight - verticalSpaceTop - verticalSpaceBottom;
        float proportionHeight = brokenLineHeight / (getMaxData() - getMinData());
        float circleCenterX = horizontalSpaceLeft;
        float circleCenterY;
        mPoints.clear();
        for (int i = 0; i < mData.size(); i++) {
            float currentProportionHeight = (mData.get(i).value - getMinData()) * proportionHeight;
            circleCenterY = brokenLineHeight - currentProportionHeight + verticalSpaceTop;
            mPoints.add(new PointF(circleCenterX, circleCenterY));
            circleCenterX += proportionWidth;
        }
    }

    private void drawBottomText(Canvas canvas) {
        float currentTextX = 0;
        float currentTextY = 0;
        currentTextX = horizontalSpaceLeft;
        currentTextY = mSelfHeight - LineChartUtils.sp2px(mContext, textSize);
        mPaint.reset();
        mPaint.setTextSize(LineChartUtils.sp2px(mContext, textSize));
        mPaint.setColor(Color.DKGRAY);
//
//        for (int i = 0; i < xAxisBasisData.size(); i++) {
//            if (i != 0 && i != xAxisBasisData.size() - 1) {
//                currentTextX += proportionWidth;
//                continue;
//            }
//
//            if (i == 0) {
//                mPaint.setTextAlign(Paint.Align.LEFT);
//                canvas.drawText(xAxisBasisData.get(i).value, currentTextX, currentTextY, mPaint);
//                currentTextX += proportionWidth;
//            } else {
//                mPaint.setTextAlign(Paint.Align.RIGHT);
//                canvas.drawText(xAxisBasisData.get(i).value, currentTextX, currentTextY, mPaint);
//                currentTextX += proportionWidth;
//            }
//        }
        mPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("09:30", currentTextX, currentTextY, mPaint);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("11:30/13:00", mSelfWidth/2, currentTextY, mPaint);
        mPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("15:00", mSelfWidth - horizontalSpaceRight, currentTextY, mPaint);
    }

    private void drawBottomLine(Canvas canvas) {
        float bottomLineStartX = horizontalSpaceLeft;
        float bottomLineStopX = mSelfWidth - horizontalSpaceRight;
        float bottomLineStartY = mSelfHeight - verticalSpaceBottom;
        float bottomLineStopY = bottomLineStartY;

        mPaint.setColor(BOTTOM_LINE_COLOR);
        mPaint.setStrokeWidth(bottomLineWidth);

        canvas.drawLine(bottomLineStartX, bottomLineStartY, bottomLineStopX, bottomLineStopY, mPaint);

        float verticalLineStartX = horizontalSpaceLeft;
        float verticalLineStartY = mSelfHeight - verticalSpaceBottom;
        float verticalLineStopX = verticalLineStartX;
        float verticalLineStopY = verticalLineStartY + bottomVerticalLineHeight;
        for (int i = 0; i < xAxisBasisData.size(); i++) {
            if (i != 0 && i != xAxisBasisData.size() - 1) {
                verticalLineStartX = verticalLineStartX + proportionWidth;
                verticalLineStopX = verticalLineStartX;
                continue;
            }
            mPaint.setStrokeWidth(bottomVerticalLineWidth);
            mPaint.setColor(BOTTOM_VERTICAL_LINE_COLOR);
            canvas.drawLine(verticalLineStartX, verticalLineStartY, verticalLineStopX, verticalLineStopY, mPaint);
            verticalLineStartX = verticalLineStartX + proportionWidth;
            verticalLineStopX = verticalLineStartX;
        }

    }

    private void drawLevelLine(Canvas canvas) {
        if (levels <= 0)
            return;

        float brokenLineHeight = mSelfHeight - verticalSpaceTop - verticalSpaceBottom;
        float proportionHeight = brokenLineHeight / (getMaxData() - getMinData());
        float oneLevelValueHeight = (getMaxData() - getMinData()) / levels * proportionHeight;

        for (int i = 0; i <= levels; ++i) {
            if (i == 0)
                continue;

            float lineStartX = horizontalSpaceLeft;
            float lineStopX = mSelfWidth - horizontalSpaceRight;
            float lineStartY = mSelfHeight - verticalSpaceBottom - i * oneLevelValueHeight;
            float lineStopY = lineStartY;

            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(LEVEL_LINE_COLOR);
            mPaint.setStrokeWidth(levelLineWidth);
            float dash = LineChartUtils.dp2px(mContext, levelDashDip);
            mPaint.setPathEffect(new DashPathEffect(new float[]{dash, dash, dash, dash}, 0));

            linePath.reset();
            linePath.moveTo(lineStartX, lineStartY);
            linePath.lineTo(lineStopX, lineStopY);

            canvas.drawPath(linePath, mPaint);
        }
    }

    private void drawLevelText(Canvas canvas) {
        if (levels < 0)
            return;

        float brokenLineHeight = mSelfHeight - verticalSpaceTop - verticalSpaceBottom;
        float proportionHeight = brokenLineHeight / (getMaxData() - getMinData());
        float oneLevelValueHeight = (getMaxData() - getMinData()) / levels * proportionHeight;

        Rect rect = new Rect();
        String text = String.format(levelFormat, getMaxData());
        mPaint.reset();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        float fontWidth = rect.width();
        float fontHeight = rect.height();

        mPaint.setColor(FONT_COLOR);
        mPaint.setTextAlign(Paint.Align.RIGHT);
        mPaint.setTextSize(LineChartUtils.dp2px(mContext, textSize));

        for (int i = 0; i <= levels; ++i) {
            float textStartX = horizontalSpaceLeft - leftTextSpace;
            float textStartY = mSelfHeight - verticalSpaceBottom - i * oneLevelValueHeight + fontHeight;
            String text2 = String.format(levelFormat, getMinData() + (getMaxData() - getMinData()) / levels * i)+"%";
            if (text2.contains("-")){
                mPaint.setColor(0xFF00880E);//green
            }else {
                mPaint.setColor(Color.RED);
            }
            canvas.drawText(text2, textStartX, textStartY, mPaint);
        }
    }

    private void drawRightText(Canvas canvas) {
        if (levels < 0)
            return;

        float brokenLineHeight = mSelfHeight - verticalSpaceTop - verticalSpaceBottom;
        float proportionHeight = brokenLineHeight / (getMaxData() - getMinData());
        float oneLevelValueHeight = (getMaxData() - getMinData()) / levels * proportionHeight;

        Rect rect = new Rect();
        String text = String.format(levelFormat, getPercentMaxData());
        mPaint.reset();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        float fontWidth = rect.width();
        float fontHeight = rect.height();

        mPaint.setColor(Color.DKGRAY);
        mPaint.setTextAlign(Paint.Align.RIGHT);
        mPaint.setTextSize(LineChartUtils.dp2px(mContext, textSize));
        Log.d(TAG, "getPercentMinData: "+getPercentMinData());
//        Math.min(min, mData.get(i).rightvalue)
        //中间层
        int m = (levels+1)/2;
        for (int i = 0; i <= levels; ++i) {
            float textStartX = mSelfWidth-leftTextSpace;
            float textStartY = mSelfHeight - verticalSpaceBottom - i * oneLevelValueHeight + fontHeight;
            if (i==m){
                String text2 = String.format(rightFormat,midStart);
                canvas.drawText(text2, textStartX, textStartY, mPaint);
            }else {
                float percent = getMinData() + (getMaxData() - getMinData()) / levels * i;

                String text2 = String.format(rightFormat, ((percent/100)+1)*midStart);
                canvas.drawText(text2, textStartX, textStartY, mPaint);
            }

        }
    }

    private void drawBrokenLine(Canvas canvas) {
        linePath.reset();
        for (int i = 0; i < mPoints.size(); i++) {
            if (i == 0) {
                linePath.moveTo(mPoints.get(i).x, mPoints.get(i).y);
            } else {
                linePath.lineTo(mPoints.get(i).x, mPoints.get(i).y);
            }
        }
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(brokenLineWidth);
        mPaint.setColor(BROKEN_LINE_COLOR);
        canvas.drawPath(linePath, mPaint);
    }

    private void drawBackground(Canvas canvas) {
        if (mPoints.isEmpty() || mPoints.size() == 1) {
            return;
        }

        float brokenLineHeight = mSelfHeight - verticalSpaceTop - verticalSpaceBottom;
        float proportionHeight = brokenLineHeight / (getMaxData() - getMinData());

        backgroundPath.reset();
        float bottomHeight = verticalSpaceBottom;
        backgroundPath.moveTo(horizontalSpaceLeft, mSelfHeight - bottomHeight);
        for (int i = 0; i < mPoints.size(); i++) {
            backgroundPath.lineTo(mPoints.get(i).x, mPoints.get(i).y);
        }
        float bgPathEndX = mPoints.get(mPoints.size() - 1).x;
        backgroundPath.lineTo(bgPathEndX, mSelfHeight - bottomHeight);
        backgroundPath.close();
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        float shaderStartX = horizontalSpaceLeft + proportionWidth / 2;
        float shaderStartY = mSelfHeight - bottomHeight - (getMaxData() - getMinData()) * proportionHeight;

        float shaderStopX = shaderStartX;
        float shaderStopY = mSelfHeight - bottomHeight;

        Shader shader = new LinearGradient(shaderStartX, shaderStartY, shaderStopX, shaderStopY,
                LINEAR_GRADIENT_START_COLOR, LINEAR_GRADIENT_STOP_COLOR, Shader.TileMode.CLAMP);

        mBackgroundPaint.setShader(shader);
        canvas.drawPath(backgroundPath, mBackgroundPaint);
    }

    private void drawMovingLine(Canvas canvas) {
        if (isDrawMoveLine) {
            if (moveIndex == -1 || moveIndex >= mData.size())
                return;

            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(movingLineWidth);
            float dash = LineChartUtils.dp2px(mContext, movingLineDashDip);
            mPaint.setColor(MOVING_LINE_COLOR);
            mPaint.setPathEffect(new DashPathEffect(new float[]{dash, dash, dash, dash}, 0));

            float lineStartX = horizontalSpaceLeft;
            float lineStopX = mSelfWidth - horizontalSpaceRight;
            linePath.reset();
            linePath.moveTo(lineStartX, moveY);
            linePath.lineTo(lineStopX, moveY);
            canvas.drawPath(linePath, mPaint);

            float lineStartY = verticalSpaceTop;
            float lineStopY = mSelfHeight - verticalSpaceBottom;
            linePath.reset();
            linePath.moveTo(moveX, lineStartY);
            linePath.lineTo(moveX, lineStopY);
            canvas.drawPath(linePath, mPaint);

            mPaint.reset();

            mPaint.setColor(POINT_OUTER_COLOR);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(moveX, moveY, movingPointRadiusOuter, mPaint);

            mPaint.setColor(POINT_INNER_COLOR);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(moveX, moveY, movingPointRadiusInner, mPaint);

            {
                // bottom
                Rect rect = new Rect();
                mTipPaint.reset();
                mTipPaint.setTextSize(LineChartUtils.sp2px(mContext, textSize));
                mTipPaint.setStyle(Paint.Style.FILL);
                mTipPaint.setColor(BOTTOM_TIP_FRAME_COLOR);
                mTipPaint.getTextBounds(xAxisBasisData.get(0).value, 0, xAxisBasisData.get(0).value.length(), rect);
                float fontWidth = rect.width();
                float fontHeight = rect.height();

                float left = moveX - fontWidth / 2 - textPadding;
                float right = moveX + fontWidth / 2 + textPadding;
                float top = mSelfHeight - verticalSpaceBottom + bottomVerticalLineHeight / 2;
                float bottom = top + textPadding * 2 + fontHeight;

                RectF rectF = new RectF(left, top, right, bottom);
                canvas.drawRoundRect(rectF, 7, 7, mTipPaint);

                mTipPaint.setStrokeWidth(8);
                mTipPaint.setTextAlign(Paint.Align.CENTER);
                mTipPaint.setColor(Color.WHITE);
                String text = mData.get(moveIndex).bottomLabel;
                Paint.FontMetrics fontMetrics = mTipPaint.getFontMetrics();
                float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
                float baseline = rectF.centerY() + distance;
                canvas.drawText(text, rectF.centerX(), baseline, mTipPaint);
            }

            {
                // left
                Rect rect = new Rect();
                mTipPaint.reset();
                mTipPaint.setTextSize(LineChartUtils.sp2px(mContext, textSize));
                mTipPaint.setStyle(Paint.Style.FILL);
                mTipPaint.setColor(LEFT_TIP_FRAME_COLOR);
                mTipPaint.getTextBounds(String.valueOf(getMaxData()), 0, String.valueOf(getMaxData()).length(), rect);
                float fontWidth = rect.width();
                float fontHeight = rect.height();

                float left = horizontalSpaceLeft - leftTextSpace - fontWidth - textPadding * 4;
                float right = horizontalSpaceLeft - leftTextSpace;
                float top = moveY - fontHeight / 2 - textPadding;
                float bottom = moveY + fontHeight / 2 + textPadding;

                RectF rectF = new RectF(left, top, right, bottom);
                canvas.drawRoundRect(rectF, 7, 7, mTipPaint);

                mTipPaint.setStrokeWidth(8);
                mTipPaint.setTextAlign(Paint.Align.CENTER);
                mTipPaint.setColor(Color.WHITE);
                String text = mData.get(moveIndex).leftLabel;
                Paint.FontMetrics fontMetrics = mTipPaint.getFontMetrics();
                float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
                float baseline = rectF.centerY() + distance;
                canvas.drawText(text+"%", rectF.centerX(), baseline, mTipPaint);
            }
            {
                // right
                Rect rect = new Rect();
                mTipPaint.reset();
                mTipPaint.setTextSize(LineChartUtils.sp2px(mContext, textSize));
                mTipPaint.setStyle(Paint.Style.FILL);
                mTipPaint.setColor(LEFT_TIP_FRAME_COLOR);
                mTipPaint.getTextBounds(String.valueOf(getMaxData()), 0, String.valueOf(getMaxData()).length(), rect);
                float fontWidth = rect.width();
                float fontHeight = rect.height();

                float left = mSelfWidth-horizontalSpaceRight - leftTextSpace - fontWidth - textPadding * 4;
                float right = mSelfWidth-horizontalSpaceRight;
                float top = moveY - fontHeight / 2 - textPadding;
                float bottom = moveY + fontHeight / 2 + textPadding;

                RectF rectF = new RectF(left, top, right, bottom);
                canvas.drawRoundRect(rectF, 7, 7, mTipPaint);

                mTipPaint.setStrokeWidth(8);
                mTipPaint.setTextAlign(Paint.Align.CENTER);
                mTipPaint.setColor(Color.WHITE);
                String text = mData.get(moveIndex).rightlabel;
                Paint.FontMetrics fontMetrics = mTipPaint.getFontMetrics();
                float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
                float baseline = rectF.centerY() + distance;
                canvas.drawText(text, rectF.centerX(), baseline, mTipPaint);
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawMoveLine = true;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isDrawMoveLine = false;
                moveIndex = -1;
                break;
        }
        countRoundPoint(x);
        invalidate();
        return true;
    }

    private float getMaxData() {
        if (getMinData_() == getMaxData_()) {
            return getMinData_() + DEFAULT_VALUE_GAP;
        }
        return getMaxData_();
    }

    private float getMinData() {
        if (getMinData_() == getMaxData_()) {
            return getMinData_() - DEFAULT_VALUE_GAP;
        }
        return getMinData_();
    }
    private float getPercentMaxData() {
        if (getMinData_percent () == getMaxData_percent()) {
            return getMinData_percent() + DEFAULT_VALUE_GAP;
        }
        return getMaxData_percent();
    }

    private float getPercentMinData() {
        if (getMinData_percent() == getMaxData_percent()) {
            return getMinData_percent() - DEFAULT_VALUE_GAP;
        }

        return getMinData_percent();
    }

    // for 1.x 2.x 3.x 4.x, set max to 4+1
    private float getMaxData_() {
        float max = Integer.MIN_VALUE;
        for (int i = 0; i < mData.size(); i++) {
            max = Math.max(max, mData.get(i).value);
        }
        if (max == Integer.MIN_VALUE) {
            return DEFAULT_MAX_VALUE;
        }
        return (float) Math.ceil(max);
    }

    // for 1.x 2.x 3.x 4.x, set min to 1
    private float getMinData_() {
        float min = Integer.MAX_VALUE;
        for (int i = 0; i < mData.size(); i++) {
            min = Math.min(min, mData.get(i).value);
        }
        if (min == Integer.MAX_VALUE) {
            return DEFAULT_MIN_VALUE;
        }
        return (float) Math.floor(min);
    }

    // for 1.x 2.x 3.x 4.x, set max to 4+1
    private float getMaxData_percent() {
        float max = Float.MIN_VALUE;
        for (int i = 0; i < mData.size(); i++) {
            max = Math.max(max, mData.get(i).rightvalue);
        }
        if (max == Float.MIN_VALUE) {
            return DEFAULT_MAX_VALUE;
        }
        return (float) Math.ceil(max);
    }

    // for 1.x 2.x 3.x 4.x, set min to 1
    private float getMinData_percent() {
        float min = Float.MAX_VALUE;
        for (int i = 0; i < mData.size(); i++) {
            min = Math.min(min, mData.get(i).rightvalue);
        }
        if (min == Float.MAX_VALUE) {
            return DEFAULT_MIN_VALUE;
        }
        return (float) Math.floor(min);
    }

    private void countRoundPoint(float x) {
        int location = Math.round((x - horizontalSpaceLeft) / proportionWidth);
        if (location < 0)
            location = 0;
        if (mData.isEmpty()) {
            moveIndex = -1;
            return;
        }
        if (mPoints.isEmpty()) {
            moveIndex = -1;
            return;
        }
        if (location >= mData.size())
            location = mData.size() - 1;

        if (location >= mPoints.size()) {
            moveIndex = -1;
            return;
        }
        moveIndex = location;

        moveX = mPoints.get(location).x;
        moveY = mPoints.get(location).y;
    }
}

package com.example.myfundkt.utils;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.myfundkt.R;


/**
 * 圆角的RelativeLayout
 */

public class RoundRelativeLayout extends RelativeLayout {

    private Path mPath;
    private int mRadius;

    private int mWidth;
    private int mHeight;
    private int mLastRadius;

    public static final int MODE_NONE = 0;
    public static final int MODE_ALL = 1;
    public static final int MODE_LEFT = 2;
    public static final int MODE_TOP = 3;
    public static final int MODE_RIGHT = 4;
    public static final int MODE_BOTTOM = 5;

    private int mRoundMode = MODE_ALL;

//    public RoundRelativeLayout(Context context) {
//        super(context);
//
//        init();
//    }

    public RoundRelativeLayout(Context context, AttributeSet attrs) {
        super (context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes (attrs, R.styleable.RoundRelativeLayout);
        int radius = typedArray.getDimensionPixelSize (R.styleable.RoundRelativeLayout_roundRadius, 30);
        int backgroudColor = typedArray.getColor(R.styleable.RoundRelativeLayout_backGroundColor, Color.TRANSPARENT);//        MyLog.d("RoundRelativeLayout", "backgroudColor: "+backgroudColor);
        typedArray.recycle (); //注意回收
        init (radius,backgroudColor);
    }

    private void init(int radius,int color) {
        mPath = new Path ();
        mPath.setFillType (Path.FillType.EVEN_ODD);
        setBackgroundColor(color);
        setCornerRadius (radius);
    }


    /**
     * 设置是否圆角裁边
     * @param roundMode
     */
    public void setRoundMode(int roundMode){
        mRoundMode = roundMode;
    }

    /**
     * 设置圆角半径
     * @param radius
     */
    public void setCornerRadius(int radius){
        mRadius = radius;
    }

    private void checkPathChanged(){

        if(getWidth() == mWidth && getHeight() == mHeight && mLastRadius == mRadius){
            return;
        }

        mWidth = getWidth();
        mHeight = getHeight();
        mLastRadius = mRadius;

        mPath.reset();

        switch (mRoundMode){
            case MODE_ALL:
                mPath.addRoundRect(new RectF (0, 0, mWidth, mHeight), mRadius, mRadius, Path.Direction.CW);
                break;
            case MODE_LEFT:
                mPath.addRoundRect(new RectF(0, 0, mWidth, mHeight),
                        new float[]{mRadius, mRadius, 0, 0, 0, 0, mRadius, mRadius},
                        Path.Direction.CW);
                break;
            case MODE_TOP:
                mPath.addRoundRect(new RectF(0, 0, mWidth, mHeight),
                        new float[]{mRadius, mRadius, mRadius, mRadius, 0, 0, 0, 0},
                        Path.Direction.CW);
                break;
            case MODE_RIGHT:
                mPath.addRoundRect(new RectF(0, 0, mWidth, mHeight),
                        new float[]{0, 0, mRadius, mRadius, mRadius, mRadius, 0, 0},
                        Path.Direction.CW);
                break;
            case MODE_BOTTOM:
                mPath.addRoundRect(new RectF(0, 0, mWidth, mHeight),
                        new float[]{0, 0, 0, 0, mRadius, mRadius, mRadius, mRadius},
                        Path.Direction.CW);
                break;
        }

    }

    @Override
    public void draw(Canvas canvas) {

        if(mRoundMode != MODE_NONE){
            int saveCount = canvas.save();

            checkPathChanged();

            canvas.clipPath(mPath);
            super.draw(canvas);

            canvas.restoreToCount(saveCount);
        }else {
            super.draw(canvas);
        }


    }
}

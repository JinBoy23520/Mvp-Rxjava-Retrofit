package com.alin.hourse.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.alin.hourse.common.R;


/**
 * @创建者 hailin
 * @创建时间 2017/9/29 12:16
 * @描述 ${}.
 */

public class CommonRelativeLayout extends RelativeLayout {
    private Drawable mBorderDrawble;
    private Drawable mTouchedDrawble;
    private boolean  mPressed;
    private float    mCornerRadius;
    private int      mSaveLayerCount;
    private RectF    mRectF;
    private Path     mPath;
    private Paint    mPaint;

    public CommonRelativeLayout(Context context) {
        this(context,null);
    }

    public CommonRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CommonRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initlialize(attrs);

    }

    private void initlialize(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CommonRelativeLayout);
            if (ta != null) {
                try {
                    mBorderDrawble = ta.getDrawable(R.styleable.CommonLinearLayout_border_drawable);
                    mTouchedDrawble = ta.getDrawable(R.styleable.CommonLinearLayout_touched_drawable);
                    mCornerRadius = ta.getDimension(R.styleable.CommonLinearLayout_corner_radius, 0);
                }catch (Throwable t){

                }finally {
                    ta.recycle();
                }
            }
        }

        mRectF = new RectF();
        mPaint = new Paint();
        mPath = new Path();
        mPaint.setAntiAlias(true);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPath.setFillType(Path.FillType.INVERSE_WINDING);
        this.setWillNotDraw(false);
        this.setLayerType(LAYER_TYPE_HARDWARE,null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        resetPath();
    }


    @Override
    public void draw(Canvas canvas) {
        int height = getHeight();
        int width = getWidth();
        if (height > 0  && width > 0 && mCornerRadius > 0) {
            mSaveLayerCount = canvas.saveLayerAlpha(0, 0, width, height, 255, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
        }
        super.draw(canvas);

        if (mBorderDrawble != null) {
            mBorderDrawble.setBounds(0,0,width,height);
            mBorderDrawble.draw(canvas);
        }

        if (mPressed && mTouchedDrawble != null) {
            mTouchedDrawble.setBounds(0,0,width,height);
            mTouchedDrawble.draw(canvas);
        }

        if (mSaveLayerCount > 0) {
            canvas.drawPath(mPath,mPaint);
            canvas.restoreToCount(mSaveLayerCount);
        }
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (this.isClickable() && pressed != mPressed) {
            mPressed = pressed;
            if (mTouchedDrawble != null) {
                invalidate();
            }
        }
    }

    private void resetPath() {
        mRectF.set(0,0,getWidth(),getHeight());
        mPath.reset();
        mPath.addRoundRect(mRectF,mCornerRadius,mCornerRadius, Path.Direction.CCW);
    }

    public float getCornerRadius() {
        return mCornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        mCornerRadius = cornerRadius;
        resetPath();
        invalidate();
    }

    public Drawable getBorderDrawble() {
        return mBorderDrawble;
    }

    public void setBorderDrawble(Drawable borderDrawble) {
        mBorderDrawble = borderDrawble == null ?  null : borderDrawble;
    }

    public Drawable getTouchedDrawble() {
        return mTouchedDrawble;
    }

    public void setTouchedDrawble(Drawable touchedDrawble) {
        mTouchedDrawble = touchedDrawble == null ?  null : touchedDrawble;
    }
}

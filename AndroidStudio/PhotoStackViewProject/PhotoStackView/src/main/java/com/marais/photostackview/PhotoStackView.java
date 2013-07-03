package com.marais.photostackview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

public class PhotoStackView extends View implements View.OnClickListener {

    private Context mContext;
    private int mPaddingLeft;
    private int mPaddingTop;
    private int mPaddingRight;
    private int mPaddingBottom;
    private int mStackSizeMin;
    private int mStackSize;
    private int mShadowSize;
    private int mSpin;
    private int mColour;
    private int mShadowColor;
    private boolean mShowStackGap;
    private boolean mShowStackFade;
    private float mTextSize;
    /* Usable space after removing padding. */
    private int mDrawWidth;
    private int mDrawHeight;
    /* Rect's for drawing. */
    private RectF mImageBack;
    private RectF mImageFront;
    private int mFadeInterval;
    /* How much to rotate between drawing each item in the stack. */
    private float mRotationInterval;
    private float mRotationPreOffset;
    private float mRotationX;
    private float mRotationY;
    private Paint mInnerPaint;
    private Paint mImageFillPaint;
    private Paint mImageBorderPaint;
    private Paint mImageShadowPaint;
    //private Paint mImageReflection;

    public PhotoStackView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PhotoStackView,
                0, 0);

        try {
            mStackSizeMin = a.getInt(R.styleable.PhotoStackView_stack_size_min, 0);
            mStackSize = a.getInt(R.styleable.PhotoStackView_stack_size_max, 6);
            mSpin = a.getInt(R.styleable.PhotoStackView_spin, 355);
            mColour = a.getColor(R.styleable.PhotoStackView_colour, Color.WHITE);
            mShadowColor = a.getColor(R.styleable.PhotoStackView_shadow_color, Color.BLACK);
            mShowStackGap = a.getBoolean(R.styleable.PhotoStackView_show_stack_gap, true);
            mShowStackFade = a.getBoolean(R.styleable.PhotoStackView_show_stack_fade, true);
        } finally {
            a.recycle();
        }

        init();

        calculateSizes(getMeasuredWidth(), getMeasuredHeight());

        this.setOnClickListener(this);
    }

    private void init() {

        /* Disable hardware acceleration for API11 and above, because BlurMaskFilter is not supported with acceleration enabled. */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mImageBack = new RectF();
        mImageFront = new RectF();

        mInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerPaint.setColor(mShadowColor);
        mTextSize = mInnerPaint.getTextSize();

        mImageFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mImageFillPaint.setColor(mColour);
        mImageFillPaint.setStyle(Paint.Style.FILL);
        //mImageFillPaint.setShadowLayer(10f, 0f, 0f, Color.DKGRAY);

        mImageBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mImageBorderPaint.setColor(Color.BLACK);
        mImageBorderPaint.setStyle(Paint.Style.STROKE);

        mImageShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mImageShadowPaint.setColor(mShadowColor);

        //mImageReflection = new Paint(Paint.ANTI_ALIAS_FLAG);
        //mImageReflection.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mShowStackFade) {
            int fadeOffset = mStackSize * mFadeInterval;
            mImageFillPaint.setAlpha(mImageFillPaint.getAlpha() - fadeOffset);
            mImageBorderPaint.setAlpha(mImageBorderPaint.getAlpha() - fadeOffset);
            mImageShadowPaint.setAlpha(mImageShadowPaint.getAlpha() - fadeOffset);
            mInnerPaint.setAlpha(mInnerPaint.getAlpha() - fadeOffset);
        }

        canvas.rotate(mRotationPreOffset + mSpin, mRotationX, mRotationY);

        for (int i = 1; i <= mStackSize; i++) {

            if (mShowStackFade) {
                mImageFillPaint.setAlpha(mImageFillPaint.getAlpha() + mFadeInterval);
                mImageBorderPaint.setAlpha(mImageBorderPaint.getAlpha() + mFadeInterval);
                mImageShadowPaint.setAlpha(mImageShadowPaint.getAlpha() + mFadeInterval);
                mInnerPaint.setAlpha(mInnerPaint.getAlpha() + mFadeInterval);
            }

            canvas.drawRect(mImageBack, mImageShadowPaint);
            canvas.drawRect(mImageBack, mImageFillPaint);
            canvas.drawRect(mImageFront, mInnerPaint);
            //canvas.drawRect(mImageBack, mImageReflection);

            if (i == mStackSize) {
                //canvas.drawText("w: " + mDrawWidth, mImageDrawOriginLeft, mImageDrawOriginTop + (mTextSize * 1), mInnerPaint);
                //canvas.drawText("h: " + mDrawHeight, mImageDrawOriginLeft, mImageDrawOriginTop + (mTextSize * 2), mInnerPaint);
                //canvas.drawText("p: " + mPaddingLeft + ", " + mPaddingTop + ", " + mPaddingRight + ", " + mPaddingBottom, mImageDrawOriginLeft, mImageDrawOriginTop + (mTextSize * 3), mInnerPaint);
                //canvas.drawText("s: " + mStackSize, mImageDrawOriginLeft, mImageDrawOriginTop + (mTextSize * 4), mInnerPaint);
                //canvas.drawText("r: " + mRotationInterval + "deg", mImageDrawOriginLeft, mImageDrawOriginTop + (mTextSize * 5), mInnerPaint);
            } else {
                //canvas.drawText("tl" + i, mImageDrawOriginLeft, mImageDrawOriginTop + (mTextSize * 1), mInnerPaint);
                canvas.rotate(mRotationInterval, mRotationX, mRotationY);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /* Get the bounds from the parent. */
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        int consumedWidth;
        int consumedHeight;

        ViewGroup.LayoutParams lp = getLayoutParams();

        switch (lp.width) {
            case ViewGroup.LayoutParams.MATCH_PARENT:
                consumedWidth = maxWidth;
                break;
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                consumedWidth = (int) (100 * getResources().getDisplayMetrics().density);
                break;
            default:
                consumedWidth = lp.width;
        }

        switch (lp.height) {
            case ViewGroup.LayoutParams.MATCH_PARENT:
                consumedHeight = maxHeight;
                break;
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                consumedHeight = (int) (100 * getResources().getDisplayMetrics().density);
                break;
            default:
                consumedHeight = lp.height;
        }

        setMeasuredDimension(resolveSize(consumedWidth, widthMeasureSpec),
                resolveSize(consumedHeight, heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateSizes(w, h);
    }

    private void calculateSizes(int w, int h) {

        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
        mPaddingRight = getPaddingRight();
        mPaddingBottom = getPaddingBottom();

        mDrawWidth = w - mPaddingLeft - mPaddingRight;
        mDrawHeight = h - mPaddingTop - mPaddingBottom;

        mRotationX = (float) mDrawWidth / 2f;
        mRotationY = (float) mDrawHeight / 2f;

        int maxThumbSize = Math.min(mDrawWidth, mDrawHeight);

        mShadowSize = Math.max((int) (maxThumbSize * 0.05), 1);
        if (mShadowSize > 0 && !isInEditMode())
            mImageShadowPaint.setMaskFilter(new BlurMaskFilter(mShadowSize, BlurMaskFilter.Blur.OUTER));

        int hyp = maxThumbSize - (mShadowSize * 2);
        float thumbSize = (float) Math.sqrt(((hyp * hyp) / 2f));

        float imageDrawOriginLeft = ((float) mDrawWidth - thumbSize) / 2f;
        float imageDrawOriginTop = ((float) mDrawHeight - thumbSize) / 2f;

        float imageInnerOffset = maxThumbSize * 0.03f;

        mImageBack.set(imageDrawOriginLeft, imageDrawOriginTop, imageDrawOriginLeft + thumbSize, imageDrawOriginTop + thumbSize);
        mImageFront.set(imageDrawOriginLeft + imageInnerOffset, imageDrawOriginTop + imageInnerOffset, imageDrawOriginLeft + thumbSize - imageInnerOffset, imageDrawOriginTop + thumbSize - (imageInnerOffset * 5));

        mStackSize = Math.max(Math.min((int) (maxThumbSize * 0.02), 6), 3);

        mRotationInterval = mShowStackGap ? 90f / (float) (mStackSize + 1) : 90f / (float) mStackSize;
        mRotationPreOffset = -(mRotationInterval * (mStackSize - 1));

        if (mShowStackFade) {
            mFadeInterval = 255 / mStackSize;
        }

        //LinearGradient gradient = new LinearGradient(imageDrawOriginLeft * 0.2f, imageDrawOriginTop, imageDrawOriginLeft * 0.8f, imageDrawOriginTop + thumbSize, new int[]{Color.argb(0, 0, 0, 0), Color.argb(75, 255, 255, 255), Color.argb(0, 0, 0, 0)}, null, Shader.TileMode.CLAMP);
        //mImageReflection.setShader(gradient);
    }

    public int getStackSize() {
        return mStackSize;
    }

    public void setStackSize(int mStackSize) {
        this.mStackSize = mStackSize;
        calculateSizes(getMeasuredWidth(), getMeasuredHeight());
        invalidate();
        requestLayout();
    }

    public int getShadowSize() {
        return mShadowSize;
    }

    public void setShadowSize(int mShadowSize) {
        this.mShadowSize = mShadowSize;
        calculateSizes(getMeasuredWidth(), getMeasuredHeight());
        invalidate();
        requestLayout();
    }

    public int getSpin() {
        return mSpin;
    }

    public void setSpin(int mRotation) {
        this.mSpin = mRotation;
        invalidate();
        requestLayout();
    }

    @Override
    public void onClick(View view) {
        mImageShadowPaint.setColor(Color.argb(255, 255, 211, 29));
        invalidate();
    }

    public boolean showStackGap() {
        return mShowStackGap;
    }

    public void setShowStackGap(boolean mShowStackGap) {
        this.mShowStackGap = mShowStackGap;
        calculateSizes(getMeasuredWidth(), getMeasuredHeight());
        invalidate();
    }

    public boolean showStackFade() {
        return mShowStackFade;
    }

    public void setShowStackFade(boolean mShowStackFade) {
        this.mShowStackFade = mShowStackFade;
        invalidate();
    }
}

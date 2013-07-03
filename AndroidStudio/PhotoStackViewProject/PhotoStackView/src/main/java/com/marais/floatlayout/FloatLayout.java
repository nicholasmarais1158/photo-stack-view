package com.marais.floatlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FloatLayout extends ViewGroup {

    public FloatLayout(Context context) {
        super(context);
    }

    public FloatLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        /* Get the bounds from the parent. */
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        int paddingWidth = getPaddingLeft() + getPaddingRight();

        /* Create masked MeasureSpecs. */
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        /* Running totals for width and height. */
        int consumedWidth = getPaddingLeft();
        int consumedHeight = getPaddingTop();
        int consumedRowWidth = getPaddingLeft();
        int consumedRowHeight = getPaddingTop();

        int drawX = getPaddingLeft();
        int drawY = getPaddingTop();

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            /* Tell this child to measure itself. */
            measureChild(child, childWidthMeasureSpec, childHeightMeasureSpec);

            /* Get the child's measured dimensions. */
            int childWidth = lp.width == LayoutParams.MATCH_PARENT ? maxWidth - paddingWidth : child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            boolean overflow = consumedRowWidth + childWidth + getPaddingRight() > maxWidth;

            if (overflow) {

                consumedWidth = Math.max(consumedWidth, consumedRowWidth);
                consumedHeight += consumedRowHeight;

                consumedRowWidth = getPaddingLeft() + childWidth;
                consumedRowHeight = childHeight;

                drawX = getPaddingLeft();
                drawY = consumedHeight;
            } else {
                consumedRowWidth += childWidth;
                consumedRowHeight = Math.max(consumedRowHeight, childHeight);
            }

            lp.x = drawX;
            lp.y = drawY;

            drawX += childWidth;
        }

        /* Add the right-hand edge padding. */
        consumedWidth += getPaddingRight();
        /* Add the last row height and the bottom edge padding. */
        consumedHeight += consumedRowHeight + getPaddingBottom();

        /* If MATCH_PARENT the be as big as possible/required (whichever is bigger). */
        if(getLayoutParams().width == LayoutParams.MATCH_PARENT) {
            consumedWidth = Math.max(consumedWidth, maxWidth);
        }

        setMeasuredDimension(resolveSize(consumedWidth, widthMeasureSpec),
                resolveSize(consumedHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(), lp.y
                    + child.getMeasuredHeight());
        }
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p.width, p.height);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        public int x;
        public int y;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
    }
}

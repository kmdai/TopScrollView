package com.kmdai.topscrollview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.OverScroller;

/**
 * Created by kmdai on 16-6-27.
 */
public class TopScrollView extends FrameLayout {
    /**
     * 滑动时间
     */
    private static final int ANIMATION_DURATION = 500;
    private OverScroller mScroller;
    private boolean isOpen;
    private View mScrollView;
    private View mBGView;
    private int mPagerHight;
    private int mLastScroll;

    public TopScrollView(Context context) {
        super(context);
        init(context, null);
    }

    public TopScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TopScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TopScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mScroller = new OverScroller(context);
    }

    @Override
    public void addView(View child) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("This can host only one scroll view");
        }

        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("This can host only one scroll view");
        }

        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("This can host only one scroll view");
        }

        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("This can host only one scroll view");
        }

        super.addView(child, index, params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() > 0) {
            mBGView = getChildAt(0);
            mPagerHight = mBGView.getHeight();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        /**
         * 动画执行的时候，不要计算滑动ｖｉｅｗ的大小和位置
         */
        if (!mScroller.isFinished()) {
            View childView = getChildAt(0);
            int cWidth = childView.getMeasuredWidth();
            int cHeight = childView.getMeasuredHeight();
            MarginLayoutParams cParams = (MarginLayoutParams) childView.getLayoutParams();
            int cl = 0, ct = 0, cr = 0, cb = 0;
            cl = cParams.leftMargin;
            ct = cParams.topMargin;
            cr = cl + cWidth;
            cb = cHeight + ct;
            childView.layout(cl, ct, cr, cb);
            return;
        }
        if (isOpen) {
            int cCount = getChildCount();
            int cWidth = 0;
            int cHeight = 0;
            MarginLayoutParams cParams = null;
            int mt = 0;
            /**
             * 遍历所有childView根据其宽和高，以及margin进行布局
             */
            for (int i = 0; i < cCount; i++) {
                View childView = getChildAt(i);
                cWidth = childView.getMeasuredWidth();
                cHeight = childView.getMeasuredHeight();
                cParams = (MarginLayoutParams) childView.getLayoutParams();
                int cl = 0, ct = 0, cr = 0, cb = 0;
                switch (i) {
                    case 0:
                        mt = cHeight;
                        cl = cParams.leftMargin;
                        ct = cParams.topMargin;
                        break;
                    case 1:
                        cl = cParams.leftMargin;
                        ct = cParams.topMargin + mt;
                        break;
                }
                cr = cl + cWidth;
                cb = cHeight + ct;
                childView.layout(cl, ct, cr, cb);
            }
        } else {
            super.onLayout(changed, left, top, right, bottom);
        }
    }

    public void open() {
        if (!mScroller.isFinished()) {
            return;
        }
        if (mBGView.getVisibility() == GONE) {
            mBGView.setVisibility(VISIBLE);
            mPagerHight = mBGView.getHeight();
        }
        if (isOpen) {
            mLastScroll = 0;
            isOpen = false;
        } else {
            isOpen = true;
        }
        mScroller.startScroll(0, 0, 0, mPagerHight, ANIMATION_DURATION);
        postAnimation();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void postAnimation() {
        postInvalidateOnAnimation();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollView == null) {
                mScrollView = getChildAt(1);
            }
            if (isOpen) {
                mScrollView.offsetTopAndBottom(mScroller.getCurrY() - mScrollView.getTop());
                if (mScrollView.getTop() == mPagerHight) {
                    mScroller.abortAnimation();
                }
            } else {
                int c = mScroller.getCurrY() - mLastScroll;
                mLastScroll = mScroller.getCurrY();
                if (mScrollView.getTop() == 0) {
                    mScroller.abortAnimation();
                    mBGView.setVisibility(GONE);
                    return;
                }
                mScrollView.offsetTopAndBottom(-c);
            }
            invalidate();
        }
    }
}

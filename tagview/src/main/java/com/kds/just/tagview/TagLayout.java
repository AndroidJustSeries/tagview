package com.kds.just.tagview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Random;

public class TagLayout  extends FrameLayout implements View.OnClickListener {
    private static final String TAG = "TagView";

    private int mWidth = 0; //TagView의 가로 크기
    private int mHeight = 0; //TagView의 높이 크기

    private int mDividerH = 0;  //Tag간 가로 간격
    private int mDividerV = 0;  //Tag간 세로 간격

    protected int mTagBackgroundResourceId = 0;   //background resource

    private OnTagItemListener mOnTagItemListener;

    public interface OnTagItemListener {
        public void OnSelected(View tagView, int position);
    }

    public void setOnTagItemListener(OnTagItemListener l) {
        mOnTagItemListener = l;
    }

    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = right - left;
        Log.e(TAG,"KDS3393_TEST_v mWidth = " + mWidth);
        mHeight = bottom - top;
        int v = getPaddingTop();
        int h = getPaddingLeft();
        int width = 0;
        int height = 0;

        int beforeMaxHeight = 0;
        for (int i=0;i<getChildCount();i++) {
            View view = getChildAt(i);
            view.setTag(i);
            Log.e(TAG,"KDS3393_TEST_v view.getHeight() 1 = " + view.getHeight());
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            width = view.getWidth();
            height = view.getHeight();
            Log.e(TAG,"KDS3393_TEST_v w = " + width + " mWidth = " + mWidth + " view w = " + (mWidth - h) + " layout w = " + (width + mDividerH + getPaddingRight()) + " height = " + height);
            if (mWidth - h < (width + mDividerH + getPaddingRight())) {
                h = getPaddingLeft();
                v = v + beforeMaxHeight + mDividerV;
            } else {
                if (beforeMaxHeight < height) {
                    beforeMaxHeight = height;
                }
            }

            view.setLeft(h);
            view.setTop(v);
            view.setRight(view.getLeft() + width);
            view.setBottom(view.getTop() + height);
            ((LayoutParams)view.getLayoutParams()).width = width;
            ((LayoutParams)view.getLayoutParams()).height = height;
            Log.e(TAG,"KDS3393_TEST_v view.getHeight() 2 = " + view.getHeight());
            h = h + width + mDividerH;
        }

        setBottom(getTop() + v + height + getPaddingTop() + getPaddingBottom());
        if (mHeight != getBottom() - getTop()) {
            getLayoutParams().height = getBottom() - getTop();
            post(new Runnable() {
                @Override
                public void run() {
                    requestLayout();
                }
            });
        }
    }

    public void setDivider(int h, int v) {
        mDividerH = h;
        mDividerV = v;
    }

    public void clear() {
        removeAllViews();
    }

    public void addTag(View v) {
        if (mTagBackgroundResourceId != 0) {
            v.setBackgroundResource(mTagBackgroundResourceId);
        }
        v.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        v.setOnClickListener(this);
        v.setTag(getChildCount());
        addView(v);
        requestLayout();
    }

    public void setTagBackground(int res) {
        mTagBackgroundResourceId = res;
        for (int i=0;i<getChildCount();i++) {
            View tv = getChildAt(i);
            tv.setBackgroundResource(mTagBackgroundResourceId);
        }
    }

    @Override
    public void onClick(View view) {
        View v = view;
        v.setSelected(!view.isSelected());
        if (mOnTagItemListener != null) {
            mOnTagItemListener.OnSelected(v, (int) v.getTag());
        }
        setSelectView(v,view.isSelected());
    }

    private void setSelectView(View v, boolean isSelected) {

    }

    public static int dp2px(float dp) {
        Resources resources = Resources.getSystem();
        float px = dp * resources.getDisplayMetrics().density;
        return (int) Math.ceil(px);
    }
}

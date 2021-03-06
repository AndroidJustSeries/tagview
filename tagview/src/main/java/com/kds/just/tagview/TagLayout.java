package com.kds.just.tagview;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TableLayout;

import androidx.annotation.InspectableProperty;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;

import static android.widget.FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY;

public class TagLayout  extends ViewGroup implements View.OnClickListener {
    private static final String TAG = "TagLayout";

    private int mWidth = 0; //TagView의 가로 크기
    private int mHeight = 0; //TagView의 높이 크기

    private int mDividerH = 0;  //Tag간 좌우 간격
    private int mDividerV = 0;  //Tag간 상하 간격
    private boolean mHorizontalSpreadInside = false;    //내부간격 균등 분배

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
        init(context,attrs);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagLayout);
        mDividerH = ta.getDimensionPixelSize(R.styleable.TagLayout_horizontalSpacing,0);
        mDividerV = ta.getDimensionPixelSize(R.styleable.TagLayout_verticalSpacing,0);
        mHorizontalSpreadInside = ta.getBoolean(R.styleable.TagLayout_horizontal_spread_inside,false);
    }

    @Override
    public TagLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new TagLayout.LayoutParams(getContext(), attrs);
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new TagLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int layoutHeight = MeasureSpec.getSize(heightMeasureSpec);


        int childPaddingTop = getPaddingTop();
        int childPaddingLeft = getPaddingLeft();

        int childWidth = 0;
        int childHeight = 0;

        int beforeMaxHeight = 0;

        int count = getChildCount();
        for (int i=0;i<count;i++) {
            View child = getChildAt(i);
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

            int w = child.getMeasuredWidth();
            if (lp.width == LayoutParams.WRAP_CONTENT) {
                childWidth = w;
            } else if (lp.width == LayoutParams.MATCH_PARENT) {
                childWidth = Math.max(layoutWidth, w);
            } else {
                childWidth = lp.width;
            }

            int h = child.getMeasuredHeight();
            if (lp.height == LayoutParams.WRAP_CONTENT) {
                childHeight = h;
            } else if (lp.height == LayoutParams.MATCH_PARENT) {
                childHeight = Math.max(childHeight, h);
            } else {
                childHeight = lp.height;
            }

            if (layoutWidth - childPaddingLeft < (childWidth + mDividerH + getPaddingRight())) {   //라인 변경
                childPaddingLeft = getPaddingLeft() + lp.leftMargin;
                childPaddingTop += beforeMaxHeight + mDividerV;
                beforeMaxHeight = childHeight;
            } else {
                if (beforeMaxHeight < childHeight) {
                    beforeMaxHeight = childHeight;
                }
                childPaddingLeft += lp.leftMargin;
            }

            childPaddingLeft += childWidth + mDividerH + lp.rightMargin;
            lp.mDisplayWidth = childWidth;
            lp.mDisplayHeight = childHeight;

        }
        layoutHeight = (childPaddingTop + beforeMaxHeight + getPaddingTop() + getPaddingBottom());

        measureChildren(MeasureSpec.makeMeasureSpec(layoutWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(layoutHeight, MeasureSpec.EXACTLY));

        setMeasuredDimension(layoutWidth, layoutHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mWidth = right - left;
        mHeight = bottom - top;
        int childPaddingTop = getPaddingTop();
        int childPaddingLeft = getPaddingLeft();
        int childWidth = 0;
        int childHeight = 0;

        int beforeMaxHeight = 0;
        ArrayList<View> lineViews = new ArrayList<>();
        for (int i=0;i<getChildCount();i++) {
            View child = getChildAt(i);
//            child.setBackgroundColor(getRandomRGB());
            child.setTag(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            childWidth = lp.mDisplayWidth;
            childHeight = lp.mDisplayHeight;
            if (mWidth - childPaddingLeft < (childWidth + mDividerH + getPaddingRight())) { //라인 변경
                if (mHorizontalSpreadInside) {
                    setHorizontalSpreadInside(lineViews,mWidth);
                }
                lineViews.clear();
                lineViews.add(child);
                childPaddingLeft = getPaddingLeft() + lp.leftMargin;
                childPaddingTop += beforeMaxHeight + mDividerV;
                beforeMaxHeight = childHeight;
            } else {
                if (beforeMaxHeight < childHeight) {
                    beforeMaxHeight = childHeight;
                }
                childPaddingLeft += lp.leftMargin;
                lineViews.add(child);
            }
            child.layout(childPaddingLeft,childPaddingTop,childPaddingLeft + childWidth,childPaddingTop + childHeight);
            childPaddingLeft += childWidth + mDividerH + lp.rightMargin;
        }
        if (mHorizontalSpreadInside) {
            setHorizontalSpreadInside(lineViews,mWidth);
        }
    }

    private void setHorizontalSpreadInside(ArrayList<View> lineViews, int width) {
        if (lineViews.size() <= 1) {
            return;
        }
        int totalViewWidth = 0;
        for (View v:lineViews) {
            totalViewWidth += v.getWidth();
        }
        int insideMargin = (width - totalViewWidth) / (lineViews.size() - 1);
        int left = getPaddingLeft();
        for (View v:lineViews) {
            v.layout(left ,v.getTop(),left + v.getWidth() ,v.getBottom());
            left = left + v.getWidth() + insideMargin;
        }
    }

    public void setDivider(int h, int v) {
        mDividerH = h;
        mDividerV = v;
        invalidate();
        requestLayout();
    }

    public void setHorizontalSpreadInside(boolean isSpreadInside) {
        mHorizontalSpreadInside = isSpreadInside;
        invalidate();
        requestLayout();
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

    public static class LayoutParams extends FrameLayout.LayoutParams {
        public LayoutParams(int width, int height) {
            super(width, height);
        }
        public LayoutParams(@NonNull Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(@NonNull ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(@NonNull ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(@NonNull FrameLayout.LayoutParams source) {
            super(source);
        }

        public int mIndex = 0;
        public int mDisplayWidth = 0;
        public int mDisplayHeight = 0;
    }

    public static int getRandomRGB() {
        Random rnd = new Random();
        int color = Color.parseColor("#" + String.format("77%02X%02X%02X", rnd.nextInt(255),rnd.nextInt(255),rnd.nextInt(255)));
        return color;
    }
}

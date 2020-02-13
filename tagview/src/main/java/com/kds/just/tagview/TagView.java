package com.kds.just.tagview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TagView extends FrameLayout implements View.OnClickListener {
    private static final String TAG = "TagView";

    private int mWidth = 0; //TagView의 가로 크기
    private int mHeight = 0; //TagView의 높이 크기

    private int mDividerH = 0;  //Tag간 가로 간격
    private int mDividerV = 0;  //Tag간 세로 간격

    private ArrayList<TextView> mTagViews = new ArrayList<>();  //Tag array

    private OnTagItemListener mOnTagItemListener;

    public interface OnTagItemListener {
        public void OnSelected(TextView tagView, int position);
    }

    public void setOnTagItemListener(OnTagItemListener l) {
        mOnTagItemListener = l;
    }

    public TagView(Context context) {
        super(context);
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = right - left;
        mHeight = bottom - top;
        int v = getPaddingTop();
        int h = getPaddingLeft();
        int width = 0;
        int height = 0;

        for (View view : mTagViews) {
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            width = view.getWidth();
            height = view.getHeight();

            if (mWidth - h <= (view.getWidth() + mDividerH + getPaddingRight())) {
                h = getPaddingLeft();
                v = v + height + mDividerV;
            }

            view.setLeft(h);
            view.setTop(v);
            view.setRight(view.getLeft() + width);
            view.setBottom(view.getTop() + height);
            h = h + width + mDividerH;
        }

        setBottom(getTop() + v + height + getPaddingTop() + getPaddingBottom());
        if (mHeight != getBottom() - getTop()) {
            getLayoutParams().height = getBottom() - getTop();
        }
    }

    public void setDivider(int h, int v) {
        mDividerH = h;
        mDividerV = v;
    }

    public void clear() {
        mTagViews.clear();
        removeAllViews();
    }

    public void addTag(String tag) {
        TextView tv = new TextView(getContext());
        tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tv.setText(tag);
        tv.setTextSize(13.3f);
        tv.setTextColor(Color.parseColor("#333333"));
        tv.setPadding(dp2px(18), dp2px(5), dp2px(18), dp2px(5));
        tv.setOnClickListener(this);
        tv.setTag(mTagViews.size());
        if (mTagBackgroundResourceId != 0) {
            tv.setBackgroundResource(mTagBackgroundResourceId);
        }
        mTagViews.add(tv);
        addView(tv);
        requestLayout();
    }

    private int mTagBackgroundResourceId = 0;

    public void setTagBackground(int res) {
        mTagBackgroundResourceId = res;
        for (TextView tv : mTagViews) {
            tv.setBackgroundResource(mTagBackgroundResourceId);
        }
    }

    public static int dp2px(float dp) {
        Resources resources = Resources.getSystem();
        float px = dp * resources.getDisplayMetrics().density;
        return (int) Math.ceil(px);
    }

    @Override
    public void onClick(View view) {
        TextView tv = (TextView) view;
        tv.setSelected(!view.isSelected());
        if (mOnTagItemListener != null) {
            mOnTagItemListener.OnSelected(tv, (int) tv.getTag());
        }
        if (tv.isSelected()) {
            tv.setTextColor(Color.parseColor("#ffffff"));
            tv.setTypeface(null, Typeface.BOLD);
        } else {
            tv.setTextColor(Color.parseColor("#333333"));
            tv.setTypeface(null, Typeface.NORMAL);
        }
    }
}

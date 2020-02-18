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

public class TagView extends TagLayout implements View.OnClickListener {
    private static final String TAG = "TagView";

    public TagView(Context context) {
        super(context);
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addTag(String tag) {
        TextView tv = new TextView(getContext());
        tv.setText(tag);
        tv.setTextSize(13.3f);
        tv.setTextColor(Color.parseColor("#333333"));
        tv.setPadding(dp2px(18), dp2px(5), dp2px(18), dp2px(5));
        tv.setOnClickListener(this);
        if (mTagBackgroundResourceId != 0) {
            tv.setBackgroundResource(mTagBackgroundResourceId);
        }
        addTag(tv);
    }
    private void setSelectView(View v, boolean isSelected) {
        TextView tv = (TextView) v;
        if (tv.isSelected()) {
            tv.setTextColor(Color.parseColor("#ffffff"));
            tv.setTypeface(null, Typeface.BOLD);
        } else {
            tv.setTextColor(Color.parseColor("#333333"));
            tv.setTypeface(null, Typeface.NORMAL);
        }
    }
}

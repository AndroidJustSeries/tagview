package com.kds.just.tagview.sample;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.kds.just.tagview.TagLayout;
import com.kds.just.tagview.TagView;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    TagView tagview = null;
    TagLayout tagLayout = null;
    int tagCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTagLayout();
        setTagView();
    }

    private void setTagLayout() {
        tagLayout = findViewById(R.id.taglayout);
        tagLayout.setDivider(10,10);
        tagLayout.setTagBackground(R.drawable.selector_tag);
        findViewById(R.id.taglayout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatCheckBox cb = new AppCompatCheckBox(MainActivity.this);
                cb.setText("Test " + tagCount++);
                cb.setPadding(10,0,10,0);
                tagLayout.addTag(cb);

            }
        });


    }

    private void setTagView() {
        tagview = findViewById(R.id.tagview);
        tagview.clear();
        tagview.setTagBackground(R.drawable.selector_tag);
        tagview.setDivider(10,10);
        tagview.setOnTagItemListener(new TagLayout.OnTagItemListener() {
            @Override
            public void OnSelected(View tagView, int position) {
                Toast.makeText(MainActivity.this,"Click Tag Pos:" + position,Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagview.addTag("Test " + tagCount++);
            }
        });
    }

}

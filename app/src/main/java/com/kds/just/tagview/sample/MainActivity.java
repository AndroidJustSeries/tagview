package com.kds.just.tagview.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kds.just.tagview.TagView;


public class MainActivity extends AppCompatActivity {

    TagView tagview = null;
    int tagCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tagview = findViewById(R.id.tagview);
        tagview.clear();
        tagview.setTagBackground(R.drawable.selector_tag);
        tagview.setDivider(10,10);
        tagview.setOnTagItemListener(new TagView.OnTagItemListener() {
            @Override
            public void OnSelected(TextView tagView, int position) {
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

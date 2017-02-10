package com.shamilov.imagemore.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.shamilov.imagemore.ImageMore;

public class ImageMoreExampleActivity extends AppCompatActivity {

    ImageMore imageMore;

    Button add;
    int index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        //imageMore = (ImageMore) findViewById(R.id.imageMore);
        //imageMore.setItems(Arrays.asList(images));
    }
}

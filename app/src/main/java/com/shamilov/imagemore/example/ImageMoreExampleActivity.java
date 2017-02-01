package com.shamilov.imagemore.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.shamilov.imagemore.ImageMore;

import java.util.Arrays;

public class ImageMoreExampleActivity extends AppCompatActivity {

    ImageMore imageMore;

    Button add;
    int index;

    String[] images = new String[]{
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/45/running.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/39/baseball.png"
    };

    String[] empty = new String[] {};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        imageMore = (ImageMore) findViewById(R.id.imageMore);
        imageMore.setItems(Arrays.asList(images));
    }
}

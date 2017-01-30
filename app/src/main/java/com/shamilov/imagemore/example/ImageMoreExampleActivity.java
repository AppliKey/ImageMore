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

    String[] images = new String[]{
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/45/running.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/39/baseball.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/43/tennis.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/41/basketball.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/38/football.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/44/golf.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/42/soccer.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/40/hockey.png"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        imageMore = (ImageMore) findViewById(R.id.imageMore);
        for (int i = 0; i < images.length; i++) {
            imageMore.addItem(images[i]);
        }
    }
}

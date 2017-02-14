package com.shamilov.imagemore.example;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.shamilov.imagemore.ImageMore;

import java.util.concurrent.ExecutionException;

public class ImageMoreExampleActivity extends AppCompatActivity {

    private static final String TAG = "ImageMoreExampleActivit";
    ImageMore imageMore;
    TextView textView;

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
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/40/hockey.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/45/running.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/39/baseball.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/43/tennis.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/41/basketball.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/38/football.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/44/golf.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/42/soccer.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/45/running.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/39/baseball.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/43/tennis.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/41/basketball.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/38/football.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/44/golf.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/42/soccer.png"
    };

    String[] empty = new String[]{};

    int[] array = new int[]{1, 2, 7, 3, 9, 3, 7, 7, 3, 1, 3, 9, 5, 6, 7, 4, 2, 4, 7, 3, 5, 12, 56};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        final Resources res = getResources();
        imageMore = (ImageMore) findViewById(R.id.imageMore);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < images.length; i++) {
                    Log.d(TAG, "onCreate: load #" + i + " item");
                    final FutureTarget<Bitmap> into = Glide.with(ImageMoreExampleActivity.this)
                            .load(images[i])
                            .asBitmap()
                            .into(res.getDimensionPixelSize(R.dimen.default_item_height), res.getDimensionPixelSize(R.dimen.default_item_height));
                    try {
                        final Bitmap bitmap = into.get();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    imageMore.addItem(bitmap);
                            }
                        });
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

    }
}

package com.shamilov.imagemore.example;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.shamilov.imagemore.ImageMore;
import com.squareup.picasso.Transformation;

import java.util.Collections;
import java.util.List;

public class ImageMoreExampleActivity extends AppCompatActivity {

    ImageMore imageMore;

    Button add;
    int index;

    String[] images = new String[]{
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/39/baseball.png",
            "https://gosportnow.s3.amazonaws.com/uploads/staging/uploads/sport/source/45/running.png",
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        imageMore = (ImageMore) findViewById(R.id.imageMore);
        for (int i = 0; i < images.length; i++) {
            imageMore.addItem(images[i]);
        }
        imageMore.setTransformationCallback(new ImageMore.OnApplyTransformationCallback() {

            @NonNull @Override public List<? extends Transformation> onApplyTransformations(ImageView iv, int index) {
                return index % 2 == 0
                        ? Collections.singletonList(new PicassoRoundedTransformation(Color.RED, 10))
                        : Collections.singletonList(new PicassoCircularTransformation());
            }
        });
    }

    public static class PicassoRoundedTransformation implements com.squareup.picasso.Transformation {

        @ColorInt private final int strokeColor;
        @Px private final int strokeWidth;

        public PicassoRoundedTransformation(@ColorInt int strokeColor, @Px int strokeWidth) {
            this.strokeColor = strokeColor;
            this.strokeWidth = strokeWidth;
        }

        @Override
        public Bitmap transform(final Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            final Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            final Paint strokePaint = new Paint();
            strokePaint.setColor(strokeColor);
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setAntiAlias(true);
            strokePaint.setStrokeWidth(strokeWidth);
            final float strokeRadius = r - strokeWidth / 2;
            canvas.drawCircle(r, r, strokeRadius, strokePaint);

            squaredBitmap.recycle();

            return bitmap;
        }

        @Override
        public String key() {
            return "stroked";
        }
    }

    public static class PicassoCircularTransformation implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            final int size = Math.min(source.getWidth(), source.getHeight());

            final int x = (source.getWidth() - size) / 2;
            final int y = (source.getHeight() - size) / 2;

            final Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            final Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            final Canvas canvas = new Canvas(bitmap);
            final Paint paint = new Paint();
            final BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            final float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}

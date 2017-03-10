package com.applikeysolutions.imagemore.example;

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

import com.applikeysolutions.imagemore.ImageMore;
import com.squareup.picasso.Transformation;

import java.util.Collections;
import java.util.List;

public class ImageMoreExampleActivity extends AppCompatActivity {

    ImageMore imageMore;

    Button add;
    int index;

    String[] images = new String[]{
            "https://images-na.ssl-images-amazon.com/images/I/7106mGW8G0L._CR0,204,1224,1224_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/51pe8goqQZL._CR0,42,632,632_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/71uIBqKXsNL._CR0,204,1224,1224_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/81M9aIxQANL._CR408,0,1063,1063_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/7106mGW8G0L._CR0,204,1224,1224_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/51pe8goqQZL._CR0,42,632,632_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/71uIBqKXsNL._CR0,204,1224,1224_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/81M9aIxQANL._CR408,0,1063,1063_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/7106mGW8G0L._CR0,204,1224,1224_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/51pe8goqQZL._CR0,42,632,632_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/71uIBqKXsNL._CR0,204,1224,1224_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/81M9aIxQANL._CR408,0,1063,1063_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/7106mGW8G0L._CR0,204,1224,1224_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/51pe8goqQZL._CR0,42,632,632_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/71uIBqKXsNL._CR0,204,1224,1224_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/81M9aIxQANL._CR408,0,1063,1063_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/7106mGW8G0L._CR0,204,1224,1224_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/51pe8goqQZL._CR0,42,632,632_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/71uIBqKXsNL._CR0,204,1224,1224_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/81M9aIxQANL._CR408,0,1063,1063_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/7106mGW8G0L._CR0,204,1224,1224_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/51pe8goqQZL._CR0,42,632,632_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/71uIBqKXsNL._CR0,204,1224,1224_UX128.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/81M9aIxQANL._CR408,0,1063,1063_UX128.jpg"
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

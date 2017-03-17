package com.applikeysolutions.imagemore.example;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.applikeysolutions.imagemore.ImageMoreView;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ImageMoreExampleActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageMoreView<Adapter> imageMore;
    private Adapter adapter;
    private Random random;
    String[] images = new String[] {
            "http://mo.agency/wp-content/uploads/2016/07/MO-Agency-Logo.png",
            "http://monicaobrist.ch/Wordpress/wp-content/uploads/2015/01/mo_rund-e1409067295250.jpg",
            "https://cdn.shopify.com/s/files/1/0248/0343/products/MC_MO_Patch_Hat-262_grande.jpg?v=1474429992",
            "http://monicaobrist.ch/Wordpress/wp-content/uploads/2015/01/mo_Eckig.jpg",
            "https://pbs.twimg.com/profile_images/570339859865153536/uzSIsr33.png",
            "http://www.openhead.com.ua/wp-content/uploads/2012/12/mo01.jpg",
            "http://mocomplements.com/wp-content/uploads/2016/02/LOGO-MO-BLANC-border.jpg",
            "https://d13yacurqjgara.cloudfront.net/users/35527/screenshots/2442843/2016_01_07_xoa_logo_02_1x.png",
            "https://t4.ftcdn.net/jpg/00/89/21/17/500_F_89211781_bW51ABvUssnwy7MjRZUE7z4YQj5lz6MQ.jpg",
            "http://www.brandcrowd.com/gallery/brands/pictures/picture14030980414251.jpg",
            "http://www.mefistobar.com/data/extra/bm_mefisto_logo_mo_thumb.png",
            "http://www.mobileobjective.net/i/mo_logo.png",
            "https://upload.wikimedia.org/wikipedia/commons/6/60/Logo_mo.jpg"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        random = new Random(images.length);
        imageMore = (ImageMoreView<Adapter>) findViewById(R.id.imageMore);

        final List<Item> items = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            items.add(new Item(getRandomMo()));
        }

        adapter = new Adapter();
        adapter.update(items);
        imageMore.setAdapter(adapter);
        findViewById(R.id.add).setOnClickListener(this);
        findViewById(R.id.remove).setOnClickListener(this);
        EditText spacing = (EditText) findViewById(R.id.spacing);
        spacing.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int spacing = s.length() == 0 ? 0 : Integer.parseInt(String.valueOf(s));
                imageMore.setMinItemSpacing(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, spacing, getResources()
                                .getDisplayMetrics()));
            }
        });
        AppCompatSpinner gravity = (AppCompatSpinner) findViewById(R.id.gravity);
        gravity.setAdapter(new ArrayAdapter<>(this, android.R.layout
                .simple_spinner_dropdown_item, com.applikeysolutions.imagemore.example.Gravity.values()));
        gravity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                imageMore.setGravity(com.applikeysolutions.imagemore.example.Gravity.values()[position].getGravity());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                adapter.add(new Item(getRandomMo()));
                break;
            case R.id.remove:
                adapter.remove(0);
                break;
            case R.id.center:
                imageMore.setGravity(Gravity.CENTER);
                break;
            case R.id.fill:
                imageMore.setGravity(Gravity.FILL);
                break;
            case R.id.start:
                imageMore.setGravity(Gravity.START);
                break;
            case R.id.end:
                imageMore.setGravity(Gravity.END);
                break;
        }
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
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP,
                    BitmapShader.TileMode.CLAMP);
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

    private String getRandomMo() {
        double num = random.nextDouble();
        return images[(int) (num * images.length)];
    }
}

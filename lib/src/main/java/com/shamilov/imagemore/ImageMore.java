package com.shamilov.imagemore;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class ImageMore extends LinearLayout {

    private static final String TAG = "ImageMore";

    private final List<String> items = new ArrayList<>();
    private int itemWidth;
    private int itemHeight;
    private int minMargin;
    private int actualMargin;
    private int maxViewCount;
    private int minWidth;
    private int minHeight;
    private TextView counterTextView;
    private ImageView[] imageViews;
    private Drawable counterDrawable;
    private OnApplyTransformationCallback mOnApplyTransformationCallback;

    @StyleRes
    private int mCounterTextAppearance;


    public ImageMore(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageMore(Context context) {
        this(context, null, 0);
    }

    public ImageMore(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (isInEditMode()) {
            return;
        }
        parseAttributes(context, attrs);
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageMore);
        minMargin = a.getDimensionPixelSize(R.styleable.ImageMore_minItemMargin, 0);
        mCounterTextAppearance = a.getResourceId(R.styleable.ImageMore_counterTextAppearance, R.style.DefaultCounterTextAppearance);
        counterDrawable = a.getDrawable(R.styleable.ImageMore_counterBackground);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        minWidth = getMinimumWidth();
        minHeight = getMinimumHeight();

        int width = getRealViewWidth(widthMode, widthSize);
        int height = getRealViewHeight(heightMode, heightSize);
        setMeasuredDimension(width, height);

        itemWidth = getMeasuredHeight();
        itemHeight = getMeasuredHeight();
        final int viewCount = (width + minMargin) / (itemWidth + minMargin);
        final int viewSpace = (viewCount * (itemWidth + minMargin)) - minMargin;
        final int freeSpace = width - viewSpace;
        if (viewCount > 0) {
            final int additionalMargin = freeSpace / viewCount;
            actualMargin = minMargin + additionalMargin;
        }
        maxViewCount = viewCount;

    }

    public void setTransformationCallback(OnApplyTransformationCallback callback) {
        mOnApplyTransformationCallback = callback;
    }

    public void addItem(final String item) {
        post(new Runnable() {
            @Override
            public void run() {
                items.add(item);
                notifyChange();
            }
        });
    }

    public void removeItem(final String item) {
        post(new Runnable() {
            @Override
            public void run() {
                items.remove(item);
                notifyChange();
            }
        });
    }

    public void setItemsCount(int count) {
        post(new Runnable() {
            @Override
            public void run() {
                notifyChange();
            }
        });
    }

    public void setItems(final List<String> items) {
        post(new Runnable() {
            @Override
            public void run() {
                ImageMore.this.items.clear();
                ImageMore.this.items.addAll(items);
                notifyChange();
            }
        });
    }

    private ImageView initSingleAvatar() {
        final ImageView userAvatar = new ImageView(getContext());
        final LinearLayout.LayoutParams layoutParams = new LayoutParams(itemWidth, itemHeight);
        layoutParams.setMarginEnd(actualMargin);
        userAvatar.setLayoutParams(layoutParams);
        return userAvatar;
    }

    private int getRealViewWidth(int widthMode, int widthSize) {
        int width = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            if (minWidth != 0) {
                width = minWidth;
            } else {
                width = ((ViewGroup) getParent()).getWidth();
            }
        }
        return width;
    }

    private int getRealViewHeight(int heightMode, int heightSize) {
        int height = 0;

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            if (minHeight != 0) {
                height = minHeight;
            } else {
                height = getResources().getDimensionPixelSize(R.dimen.default_item_height);
            }
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            if (minWidth != 0) {
                height = minHeight;
            } else {
                height = getResources().getDimensionPixelSize(R.dimen.default_item_height);
            }
        }
        return height;
    }

    private void initAvatarViews(int mMaxViewCount) {
        imageViews = new ImageView[mMaxViewCount];
        final int lastAvatarIndex = mMaxViewCount - 1;
        for (int i = 0; i < mMaxViewCount; i++) {
            imageViews[i] = initSingleAvatar();
            if (i < lastAvatarIndex) {
                this.addView(imageViews[i]);
            }
        }
        counterTextView = initCounter();
        final ImageView lastAvatar = imageViews[lastAvatarIndex];
        final FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutTransition(new LayoutTransition());
        final LinearLayout.LayoutParams frameLayoutParams = new LayoutParams(itemWidth, itemHeight);
        frameLayout.setLayoutParams(frameLayoutParams);
        frameLayout.addView(lastAvatar);
        frameLayout.addView(counterTextView);
        this.addView(frameLayout);
    }

    private TextView initCounter() {
        final TextView textView = new TextView(getContext());
        final LinearLayout.LayoutParams layoutParams = new LayoutParams(itemWidth, itemHeight);
        textView.setTextAppearance(getContext(), mCounterTextAppearance);
        textView.setLayoutParams(layoutParams);
        textView.setBackground(counterDrawable);
        textView.setGravity(Gravity.CENTER);
        textView.setVisibility(GONE);
        return textView;
    }

    private int getDisplayableCounterValue(int totalAddedItems, int maxVisibleItems) {
        return totalAddedItems - maxVisibleItems + 1;
    }

    private void displayUserAvatarsInActivatedViews(List<String> items, int activatedViewsCount) {
        for (int i = 0; i < activatedViewsCount; i++) {
            final ImageView imageView = imageViews[i];
            imageView.setVisibility(VISIBLE);
            RequestCreator requestCreator = Picasso.with(getContext())
                    .load(items.get(i));
            if (mOnApplyTransformationCallback != null) {
                requestCreator.transform(mOnApplyTransformationCallback.onApplyTransformations(imageView, i));
            }
            requestCreator.into(imageView);
        }
    }

    private int getCountOfActivatedViews(int dataSize) {
        int count = dataSize < imageViews.length ? dataSize : imageViews.length;
        if (isCounterVisible(dataSize)) {
            count--;
        }
        return count;
    }

    private void showCounterIfNeeded(boolean isNeedToShowCounter, int totalUsersCount) {
        if (isNeedToShowCounter) {
            if (counterTextView.getVisibility() != VISIBLE) {
                counterTextView.setVisibility(VISIBLE);
            }
            counterTextView.setText(getContext().getString(R.string.additional_item_count,
                    getDisplayableCounterValue(totalUsersCount, imageViews.length)));
        } else {
            getLastUserAvatar().setVisibility(VISIBLE);
            counterTextView.setVisibility(GONE);
        }
    }

    private void removeUnnecessaryViews(int lastVisibleViewIndex) {
        for (int i = lastVisibleViewIndex; i < imageViews.length; i++) {
            imageViews[i].setVisibility(GONE);
        }
    }

    private boolean isCounterVisible(int dataSize) {
        return dataSize > imageViews.length;
    }

    public ImageView getLastUserAvatar() {
        return imageViews[imageViews.length - 1];
    }

    private void notifyChange() {
        if (maxViewCount <= 0) return;
//        if (imageViews == null) initAvatarViews(maxViewCount);
        showCounterIfNeeded(isCounterVisible(items.size()), items.size());
        final int activatedViewsCount = getCountOfActivatedViews(items.size());
        displayUserAvatarsInActivatedViews(items, activatedViewsCount);
        removeUnnecessaryViews(activatedViewsCount);
        invalidate();
    }

    public interface OnApplyTransformationCallback {

        @NonNull
        List<? extends Transformation> onApplyTransformations(ImageView imageView, int position);
    }
}
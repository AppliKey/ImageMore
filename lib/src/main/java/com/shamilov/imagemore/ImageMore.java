package com.shamilov.imagemore;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ImageMore extends LinearLayout {

    private static final String TAG = "ImageMore";

    private int mItemWidth;
    private int mItemHeight;

    private int mMinMargin;
    private int mActualMargin;

    private int mMaxViewCount;

    private int mMinWidth;
    private int mMinHeight;

    private TextView mCounter;
    private ImageView[] mUserAvatars;
    private final List<Bitmap> items = new ArrayList<>();

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
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        setLayoutTransition(new LayoutTransition());
        setVisibility(VISIBLE);
        parseAttributes(context, attrs);

    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageMore);
        mMinMargin = typedArray.getDimensionPixelSize(R.styleable.ImageMore_minItemMargin, 0);
        mCounterTextAppearance = typedArray.getResourceId(R.styleable.ImageMore_counterTextAppearance, R.style.DefaultCounterTextAppearance);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: ");
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        mMinWidth = getMinimumWidth();
        mMinHeight = getMinimumHeight();

        int width = getRealViewWidth(widthMode, widthSize);
        int height = getRealViewHeight(heightMode, heightSize);
        setMeasuredDimension(width, height);

        mItemWidth = getMeasuredHeight();
        mItemHeight = getMeasuredHeight();
        final int viewCount = (width + mMinMargin) / (mItemWidth + mMinMargin);
        final int viewSpace = (viewCount * (mItemWidth + mMinMargin)) - mMinMargin;
        final int freeSpace = width - viewSpace;
        if (viewCount > 0) {
            final int additionalMargin = freeSpace / viewCount;
            mActualMargin = mMinMargin + additionalMargin;
        }
        mMaxViewCount = viewCount;

    }

    private int getRealViewHeight(int heightMode, int heightSize) {
        int height = 0;

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST){
            if (mMinHeight != 0) {
                height = mMinHeight;
            } else {
                height = getResources().getDimensionPixelSize(R.dimen.default_item_height);
            }
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            if (mMinWidth != 0) {
                height = mMinHeight;
            } else {
                height = getResources().getDimensionPixelSize(R.dimen.default_item_height);
            }
        }
        return height;
    }

    private int getRealViewWidth(int widthMode, int widthSize) {
        int width = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST){
            width = widthSize;
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            if (mMinWidth != 0) {
                width = mMinWidth;
            } else {
                width = ((ViewGroup) getParent()).getWidth();
            }
        }
        return width;
    }

    private void initAvatarViews(int mMaxViewCount) {
        mUserAvatars = new ImageView[mMaxViewCount];
        final int lastAvatarIndex = mMaxViewCount - 1;
        for (int i = 0; i < mMaxViewCount; i++) {
            mUserAvatars[i] = initSingleAvatar();
            if (i < lastAvatarIndex) {
                this.addView(mUserAvatars[i]);
            }
        }
        mCounter = initCounter();
        final ImageView lastAvatar = mUserAvatars[lastAvatarIndex];
        final FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutTransition(new LayoutTransition());
        final LinearLayout.LayoutParams frameLayoutParams = new LayoutParams(mItemWidth, mItemHeight);
        frameLayout.setLayoutParams(frameLayoutParams);
        frameLayout.addView(lastAvatar);
        frameLayout.addView(mCounter);
        this.addView(frameLayout);
    }

    private TextView initCounter() {
        final TextView textView = new TextView(getContext());
        final LinearLayout.LayoutParams layoutParams = new LayoutParams(mItemWidth, mItemHeight);
        textView.setTextAppearance(getContext(), mCounterTextAppearance);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setVisibility(GONE);
        return textView;
    }

    private ImageView initSingleAvatar() {
        final ImageView userAvatar = new ImageView(getContext());
        final LinearLayout.LayoutParams layoutParams = new LayoutParams(mItemWidth, mItemHeight);
        layoutParams.setMarginEnd(mActualMargin);
        userAvatar.setLayoutParams(layoutParams);
        return userAvatar;
    }

    public void addItem(final Bitmap item) {
        post(new Runnable() {
            @Override
            public void run() {
                items.add(item);
                notifyChange();
            }
        });
    }

    public void removeItem(final Bitmap item) {
        post(new Runnable() {
            @Override
            public void run() {
                items.remove(item);
                notifyChange();
            }
        });

    }

    public void setItems(final List<Bitmap> items) {
        post(new Runnable() {
            @Override
            public void run() {
                ImageMore.this.items.clear();
                ImageMore.this.items.addAll(items);
                notifyChange();
            }
        });
    }

    private int getDisplayableCounterValue(int totalAddedItems, int maxVisibleItems) {
        return totalAddedItems - maxVisibleItems + 1;
    }

    private void displayUserAvatarsInActivatedViews(List<Bitmap> items, int activatedViewsCount) {
        for (int i = 0; i < activatedViewsCount; i++) {
            final ImageView imageView = mUserAvatars[i];
            imageView.setVisibility(VISIBLE);
            imageView.setImageDrawable(new BitmapDrawable(getResources(), items.get(i)));
        }
    }

    private int getCountOfActivatedViews(int dataSize) {
        int count = dataSize < mUserAvatars.length ? dataSize : mUserAvatars.length;
        if (isCounterVisible(dataSize)) {
            count--;
        }
        return count;
    }

    private void showCounterIfNeeded(boolean isNeedToShowCounter, int totalUsersCount) {
        if (isNeedToShowCounter) {
            if (mCounter.getVisibility() != VISIBLE) {
                mCounter.setVisibility(VISIBLE);
            }
            mCounter.setText(getContext().getString(R.string.additional_item_count,
                    getDisplayableCounterValue(totalUsersCount, mUserAvatars.length)));
        } else {
            getLastUserAvatar().setVisibility(VISIBLE);
            mCounter.setVisibility(GONE);
        }
    }

    private void removeUnnecessaryViews(int lastVisibleViewIndex) {
        for (int i = lastVisibleViewIndex; i < mUserAvatars.length; i++) {
            mUserAvatars[i].setVisibility(GONE);
        }
    }

    private boolean isCounterVisible(int dataSize) {
        return dataSize > mUserAvatars.length;
    }

    public ImageView getLastUserAvatar() {
        return mUserAvatars[mUserAvatars.length - 1];
    }

    private void setVisible(boolean isActive) {
        this.setVisibility(isActive ? VISIBLE : GONE);
    }

    private boolean isNeedToBeShown(int dataSize) {
        return dataSize != 0;
    }

    private void notifyChange() {
        if (mMaxViewCount <= 0) return;

        if (mUserAvatars == null) {
            initAvatarViews(mMaxViewCount);
        }
        setVisible(isNeedToBeShown(items.size()));
        showCounterIfNeeded(isCounterVisible(items.size()), items.size());
        final int activatedViewsCount = getCountOfActivatedViews(items.size());
        displayUserAvatarsInActivatedViews(items, activatedViewsCount);
        removeUnnecessaryViews(activatedViewsCount);
    }
}
package com.shamilov.imagemore;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageMore extends LinearLayout {

    private int mItemWidth;
    private int mItemHeight;
    private int mMinMargin;
    private int mActualMargin;
    private int mMaxViewCount;
    private TextView mCounter;
    private ImageView[] mUserAvatars;
    private final List<String> items = new ArrayList<>();
    private PicassoCircularTransformation circularTransformation;

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
        circularTransformation = new PicassoCircularTransformation();

    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageMore);
        mMinMargin = typedArray.getDimensionPixelSize(R.styleable.ImageMore_minItemMargin, 0);
        mCounterTextAppearance = typedArray.getResourceId(R.styleable.ImageMore_counterTextAppearance, R.style.DefaultCounterTextAppearance);
        typedArray.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        mItemWidth = MeasureSpec.getSize(heightMeasureSpec);
        mItemHeight = mItemWidth;
        final int viewCount = (width + mMinMargin) / (mItemWidth + mMinMargin);
        final int viewSpace = (viewCount * (mItemWidth + mMinMargin)) - mMinMargin;
        final int freeSpace = width - viewSpace;
        final int additionalMargin = freeSpace / viewCount;
        mActualMargin = mMinMargin + additionalMargin;
        mMaxViewCount = viewCount;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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

    private int getDisplayableCounterValue(int totalAddedItems, int maxVisibleItems) {
        return totalAddedItems - maxVisibleItems + 1;
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

    private void displayUserAvatarsInActivatedViews(List<String> items, int activatedViewsCount) {
        for (int i = 0; i < activatedViewsCount; i++) {
            final ImageView imageView = mUserAvatars[i];
            imageView.setVisibility(VISIBLE);
            Picasso.with(getContext())
                    .load(items.get(i))
                    .transform(circularTransformation)
                    .into(imageView);
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
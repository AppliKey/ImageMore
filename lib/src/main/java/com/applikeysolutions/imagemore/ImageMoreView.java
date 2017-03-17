package com.applikeysolutions.imagemore;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

public class ImageMoreView<T extends BaseAdapter> extends AdapterView<T> {

    private static final String TAG = "ImageMoreAdapterView";

    private T adapter;
    private int minItemSpacing;
    private int gravity = Gravity.FILL;
    private boolean isMoreShowed;
    private int itemSize;

    private DataSetObserver adapterDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyViewChanged();
        }
    };

    public ImageMoreView(Context context) {
        super(context);
    }

    public ImageMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        obtainAttr(attrs, 0, 0);
    }

    public ImageMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttr(attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageMoreView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        obtainAttr(attrs, defStyleAttr, defStyleRes);
    }

    private void obtainAttr(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.ImageMoreView, defStyleAttr, defStyleRes);
        try {
            this.minItemSpacing = typedArray.getDimensionPixelSize(R.styleable.ImageMoreView_minItemSpacing, 0);
            this.gravity = typedArray.getInteger(R.styleable.ImageMoreView_gravity, Gravity.FILL);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    public T getAdapter() {
        return adapter;
    }

    @Override
    public void setAdapter(T adapter) {
        this.adapter = adapter;
        this.adapter.registerDataSetObserver(adapterDataSetObserver);
        notifyViewChanged();
    }

    @Override
    public void setSelection(int position) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public View getSelectedView() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // if we don't have an adapter, we don't need to do anything
        if (adapter == null) {
            return;
        }

        if (getChildCount() == 0) {
            int position = 0;
            int bottomEdge = getPaddingStart();
            int measureWidth = 0;
            View newRightChild = null;
            while ((bottomEdge + measureWidth + getPaddingEnd()) < (getWidth() - getPaddingEnd())
                    && position < adapter.getCount()) {
                newRightChild = adapter.getView(position, null, this);
                addAndMeasureChild(newRightChild);
                measureWidth = newRightChild.getMeasuredWidth();
                bottomEdge += measureWidth;
                bottomEdge += minItemSpacing;
                position++;
            }
            if (adapter instanceof ImageMoreAdapter && newRightChild != null
                    && (bottomEdge + measureWidth + getPaddingEnd()) >= (getWidth() - getPaddingEnd())) {
                removeViewInLayout(newRightChild);
                newRightChild = ((ImageMoreAdapter) adapter).getMoreView(adapter.getCount() - getChildCount(), null, this);
                addAndMeasureChild(newRightChild);
                isMoreShowed = true;
            } else {
                isMoreShowed = false;
            }
        }
        positionItems();
    }

    public int getMinItemSpacing() {
        return minItemSpacing;
    }

    public void setMinItemSpacing(int minItemSpacing) {
        this.minItemSpacing = minItemSpacing;
        notifyViewChanged();
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
        notifyViewChanged();
    }

    /**
     * Adds a view as a child view and takes care of measuring it
     *
     * @param child The view to add
     */
    private void addAndMeasureChild(View child) {
        LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
        addViewInLayout(child, -1, params, true);

        itemSize = getContentHeight();
        child.measure(MeasureSpec.EXACTLY | itemSize, MeasureSpec.EXACTLY | itemSize);
    }

    /**
     * Positions the children at the "correct" positions
     */
    private void positionItems() {
        final int childCount = getChildCount();
        int left = getPaddingStart();
        if (getFixedGravity() == Gravity.CENTER) {
            left += (getContentWidth() - childCount * (itemSize + minItemSpacing) + minItemSpacing) / 2;
        } else if (getFixedGravity() == Gravity.END) {
            left += (getContentWidth() - childCount * (itemSize + minItemSpacing) + minItemSpacing);
        }
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();
            final int top = (getHeight() - childHeight) / 2;
            final int contentWidth = getContentWidth();
            final int itemSpacing = getItemSpacing(childCount, contentWidth, childWidth);

            child.layout(left, top, left + childWidth, top + childHeight);

            if (!isLastChild(i)) {
                left += childWidth + itemSpacing;
            }
        }
    }

    private boolean isLastChild(int position) {
        return position == getChildCount() - 1;
    }

    private int getItemSpacing(int childCount, int realWidth, int childWidth) {
        final int gravity = getFixedGravity();
        if (gravity == Gravity.FILL) {
            return childCount <= 1 ? 0 : (realWidth - childWidth * childCount) / (childCount - 1);
        } else {
            return minItemSpacing;
        }
    }

    private int getFixedGravity() {
        return isMoreShowed ? Gravity.FILL : gravity;
    }

    private int getContentWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getContentHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    private void notifyViewChanged() {
        removeAllViewsInLayout();
        requestLayout();
        invalidate();
    }
}

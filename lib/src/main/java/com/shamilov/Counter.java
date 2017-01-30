package com.shamilov;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class Counter extends TextView {

    public Counter(Context context) {
        super(context);
        init(context, null, 0);
    }

    public Counter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public Counter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

    }
}

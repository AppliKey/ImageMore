package com.applikeysolutions.imagemore.example;

public enum Gravity {
    START(android.view.Gravity.START),
    END(android.view.Gravity.END),
    FILL(android.view.Gravity.FILL),
    CENTER(android.view.Gravity.CENTER);

    private final int gravity;

    Gravity(int gravity) {
        this.gravity = gravity;
    }

    public int getGravity() {
        return gravity;
    }
}

package com.gzsll.hupu.widget;

/**
 * Created by sll on 2015/9/16.
 */
public enum SwipyRefreshLayoutDirection {

    TOP(0),
    BOTTOM(1),
    BOTH(2);

    private int mValue;

    SwipyRefreshLayoutDirection(int value) {
        this.mValue = value;
    }

    public static SwipyRefreshLayoutDirection getFromInt(int value) {
        for (SwipyRefreshLayoutDirection direction : SwipyRefreshLayoutDirection.values()) {
            if (direction.mValue == value) {
                return direction;
            }
        }
        return BOTH;
    }
}

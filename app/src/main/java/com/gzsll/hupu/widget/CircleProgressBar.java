package com.gzsll.hupu.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.gzsll.hupu.R;


/**
 * Created by sll on 2015/5/25.
 */
public class CircleProgressBar extends View {

    /**
     * circle radius
     */
    private int radius = -1;

    /**
     * progress background color
     */
    private int progressBackground = Color.WHITE;

    /**
     * progress color
     */
    private int progressColor = Color.GRAY;

    /**
     * max progress
     */
    private int max = -1;

    /**
     * current progress
     */
    private int progress = -1;

    public CircleProgressBar(Context context) {
        super(context);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.cpb);
        radius = array.getDimensionPixelSize(R.styleable.cpb_radius, -1);
        progressBackground = array.getColor(R.styleable.cpb_progress_background, Color.WHITE);
        progressColor = array.getColor(R.styleable.cpb_progress_color, Color.GRAY);
        array.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (radius != -1 && getVisibility() == VISIBLE) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(progressBackground);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);

            float angle = 0;
            if (max > 0 && progress >= 0) {
                angle = (float) progress / (float) max * 360f;
            }
            paint.setColor(progressColor);
            RectF oval2 = new RectF(((float) getWidth() - radius * 2) / 2f, ((float) getHeight() - radius * 2f) / 2f, (float) getWidth() - ((float) getWidth() - radius * 2) / 2f, (float) getHeight() - ((float) getHeight() - radius * 2f) / 2f);
            canvas.drawArc(oval2, 270, angle, true, paint);
        }

    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            invalidate();
        }
    }

    /**
     * set radius
     *
     * @param radius
     * @attr ref R.styleable#radius
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * set progress background color
     *
     * @param progressBackground
     * @attr ref R.styleable#progress_background
     */
    public void setProgressBackground(int progressBackground) {
        this.progressBackground = progressBackground;
    }

    /**
     * set progress color
     *
     * @param progressColor
     * @attr ref R.styleable#progress_color
     */
    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    /**
     * set max progress value
     *
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * set progress value
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}
package com.gzsll.hupu.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.facebook.drawee.drawable.ProgressBarDrawable;

/**
 * Created by sll on 2016/4/1.
 */
public class ImageLoadProgressBar extends ProgressBarDrawable {

    private float level;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    final RectF oval = new RectF();

    int radius = 60;

    private OnLevelChangeListener listener;

    public ImageLoadProgressBar() {
        this(null);
    }

    public ImageLoadProgressBar(OnLevelChangeListener listener) {
        this(listener, Color.GRAY);
    }

    public ImageLoadProgressBar(OnLevelChangeListener listener, int color) {
        this.listener = listener;
        paint.setColor(color);
    }

    @Override
    protected boolean onLevelChange(int level) {
        this.level = level;
        if (listener != null) {
            listener.onChange(level);
        }
        invalidateSelf();
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        oval.set(canvas.getWidth() / 2 - radius, canvas.getHeight() / 2 - radius,
                canvas.getWidth() / 2 + radius, canvas.getHeight() / 2 + radius);
        drawCircle(canvas, level);
    }

    private void drawCircle(Canvas canvas, float level) {
        float angle = level / 10000 * 360f;
        canvas.drawArc(oval, 270, angle, true, paint);
    }

    public interface OnLevelChangeListener {
        void onChange(int level);
    }
}
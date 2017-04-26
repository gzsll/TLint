package com.gzsll.hupu.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.gzsll.hupu.R;

public class PagePicker extends PopupWindow implements View.OnClickListener {

    private OnJumpListener mListener;

    private MaterialNumberPicker picker;

    public PagePicker(Context context) {
        this(context, null);
    }

    public PagePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new BitmapDrawable());// 这样设置才能点击屏幕外dismiss窗口
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.popwindow_anim_style);

        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View rootView = mLayoutInflater.inflate(R.layout.page_picker_view, null);
        rootView.findViewById(R.id.btCancel).setOnClickListener(this);
        rootView.findViewById(R.id.btFirst).setOnClickListener(this);
        rootView.findViewById(R.id.btJump).setOnClickListener(this);
        rootView.findViewById(R.id.btLast).setOnClickListener(this);
        picker = (MaterialNumberPicker) rootView.findViewById(R.id.picker);
        setContentView(rootView);
    }

    void jump() {
        int page = picker.getValue();
        if (mListener != null) {
            mListener.OnJump(page);
        }
        dismiss();
    }

    void end() {
        int page = picker.getMaxValue();
        if (mListener != null) {
            mListener.OnJump(page);
        }
        dismiss();
    }

    void first() {
        int page = picker.getMinValue();
        if (mListener != null) {
            mListener.OnJump(page);
        }
        dismiss();
    }

    public void setMin(int min) {
        picker.setMinValue(min);
    }

    public void setMax(int max) {
        picker.setMaxValue(max);
    }

    public void setValue(int value) {
        picker.setValue(value);
    }

    public void setOnJumpListener(OnJumpListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btCancel:
                dismiss();
                break;
            case R.id.btFirst:
                first();
                break;
            case R.id.btLast:
                end();
                break;
            case R.id.btJump:
                jump();
                break;
        }
    }

    public interface OnJumpListener {
        void OnJump(int page);
    }
}

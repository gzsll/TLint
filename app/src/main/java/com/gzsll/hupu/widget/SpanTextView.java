package com.gzsll.hupu.widget;

import android.content.Context;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sll on 2015/11/20.
 */
public class SpanTextView extends TextView {
    public SpanTextView(Context context) {
        super(context);
        setMovementMethod(TouchableMovementMethod.getInstance());
    }

    public SpanTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMovementMethod(TouchableMovementMethod.getInstance());
    }


    public void setTextViewHTML(String str) {
        setText(new SpannableStringBuilder(Html.fromHtml(str)));
    }


}

package com.gzsll.hupu.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.DynamicDrawableSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

/**
 * 图文混排的TextView，解决提前换行造成排版混乱
 * created by sll
 */
public class JustifyTextView extends TextView {

    /**
     * 缓存测量过的数据
     */
    private static HashMap<String, SoftReference<MeasuredData>> measuredData = new HashMap<String, SoftReference<MeasuredData>>();
    private static int hashIndex = 0;
    /**
     * 存储当前文本内容，每个item为一行
     */
    ArrayList<LINE> contentList = new ArrayList<LINE>();
    private Context context;
    /**
     * 用于测量字符宽度
     */
    private TextPaint paint = new TextPaint();
    /**
     * 用于测量span高度
     */
    private Paint.FontMetricsInt mSpanFmInt = new Paint.FontMetricsInt();
    /**
     * 临时使用,以免在onDraw中反复生产新对象
     */
    private Paint.FontMetrics mFontMetrics = new Paint.FontMetrics();

    //	private float lineSpacingMult = 0.5f;
    private int textColor = Color.BLACK;
    //行距
    private float lineSpacing;
    private int lineSpacingDP = 5;
    /**
     * 段间距,-1为默认
     */
    private int paragraphSpacing = -1;
    /**
     * 最大宽度
     */
    private int maxWidth;
    /**
     * 只有一行时的宽度
     */
    private int oneLineWidth = -1;
    /**
     * 已绘的行中最宽的一行的宽度
     */
    private float lineWidthMax = -1;
    /**
     * 存储当前文本内容,每个item为一个字符或者一个SpanObject
     */
    private ArrayList<Object> obList = new ArrayList<Object>();
    /**
     * 是否使用默认{@link #onMeasure(int, int)}和{@link #onDraw(Canvas)}
     */
    private boolean useDefault = false;
    protected CharSequence text = "";

    private int maxLine = Integer.MAX_VALUE;

    private int minHeight;
    /**
     * 用以获取屏幕高宽
     */
    private DisplayMetrics displayMetrics;
    /**
     * {@link BackgroundColorSpan}用
     */
    private Paint textBgColorPaint = new Paint();
    /**
     * {@link BackgroundColorSpan}用
     */
    private Rect textBgColorRect = new Rect();

    public JustifyTextView(Context context) {
        super(context);
        init(context);
    }

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public JustifyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    public void init(Context context) {
        this.context = context;
        paint.setAntiAlias(true);
        lineSpacing = dip2px(context, lineSpacingDP);
        minHeight = dip2px(context, 30);

        displayMetrics = new DisplayMetrics();
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void setMaxWidth(int maxpixels) {
        super.setMaxWidth(maxpixels);
        maxWidth = maxpixels;
    }

    @Override
    public void setMinHeight(int minHeight) {
        super.setMinHeight(minHeight);
        this.minHeight = minHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (useDefault) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int width = 0, height = 0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:

                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                width = displayMetrics.widthPixels;
                break;
            default:
                break;
        }
        if (maxWidth > 0) {
            width = Math.min(width, maxWidth);
        }

        paint.setTextSize(this.getTextSize());
        paint.setColor(textColor);
        int realHeight = measureContentHeight((int) width);

        //如果实际行宽少于预定的宽度，减少行宽以使其内容横向居中
        int leftPadding = getCompoundPaddingLeft();
        int rightPadding = getCompoundPaddingRight();
        width = Math.min(width, (int) lineWidthMax + leftPadding + rightPadding);

        if (oneLineWidth > -1) {
            width = oneLineWidth;
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = realHeight;
                break;
            case MeasureSpec.UNSPECIFIED:
                height = realHeight;
                break;
            default:
                break;
        }

        height += getCompoundPaddingTop() + getCompoundPaddingBottom();

        height = Math.max(height, minHeight);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (useDefault) {
            super.onDraw(canvas);
            return;
        }
        if (contentList.isEmpty()) {
            return;
        }
        int width;

        Object ob;

        int leftPadding = getCompoundPaddingLeft();
        int topPadding = getCompoundPaddingTop();

        float height = 0 + topPadding + lineSpacing;
        //只有一行时
        if (oneLineWidth != -1) {
            height = getMeasuredHeight() / 2 - contentList.get(0).height / 2;
        }
        int size = contentList.size();
        if (size > maxLine) {
            size = maxLine;
        }
        for (int i = 0; i < size; i++) {
            LINE aContentList = contentList.get(i);
            //绘制一行
            float realDrawedWidth = leftPadding;
            /** 是否换新段落*/
            boolean newParagraph = false;
            for (int j = 0; j < aContentList.line.size(); j++) {
                ob = aContentList.line.get(j);
                width = aContentList.widthList.get(j);

                paint.getFontMetrics(mFontMetrics);
                float x = realDrawedWidth;
                float y = height + aContentList.height - paint.getFontMetrics().descent;
                float top = y - aContentList.height;
                float bottom = y + mFontMetrics.descent;
                if (ob instanceof String) {
                    canvas.drawText((String) ob, realDrawedWidth, y, paint);
                    realDrawedWidth += width;
                    if (((String) ob).endsWith("\n") && j == aContentList.line.size() - 1) {
                        newParagraph = true;
                    }
                } else if (ob instanceof SpanObject) {
                    Object span = ((SpanObject) ob).span;
                    if (span instanceof DynamicDrawableSpan) {

                        int start = ((Spannable) text).getSpanStart(span);
                        int end = ((Spannable) text).getSpanEnd(span);
                        ((DynamicDrawableSpan) span).draw(canvas, text, start, end, (int) x, (int) top, (int) y, (int) bottom, paint);
                        realDrawedWidth += width;
                    } else if (span instanceof BackgroundColorSpan) {

                        textBgColorPaint.setColor(((BackgroundColorSpan) span).getBackgroundColor());
                        textBgColorPaint.setStyle(Paint.Style.FILL);
                        textBgColorRect.left = (int) realDrawedWidth;
                        int textHeight = (int) getTextSize();
                        textBgColorRect.top = (int) (height + aContentList.height - textHeight - mFontMetrics.descent);
                        textBgColorRect.right = textBgColorRect.left + width;
                        textBgColorRect.bottom = (int) (height + aContentList.height + lineSpacing - mFontMetrics.descent);
                        canvas.drawRect(textBgColorRect, textBgColorPaint);
                        canvas.drawText(((SpanObject) ob).source.toString(), realDrawedWidth, height + aContentList.height - mFontMetrics.descent, paint);
                        realDrawedWidth += width;
                    } else//做字符串处理
                    {
                        canvas.drawText(((SpanObject) ob).source.toString(), realDrawedWidth, height + aContentList.height - mFontMetrics.descent, paint);
                        realDrawedWidth += width;
                    }
                }

            }
            //如果要绘制段间距
            if (newParagraph) {
                height += aContentList.height + paragraphSpacing;
            } else {
                height += aContentList.height + lineSpacing;
            }
        }

    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        textColor = color;
    }

    /**
     * 用于带ImageSpan的文本内容所占高度测量
     *
     * @param width 预定的宽度
     * @return 所需的高度
     */
    private int measureContentHeight(int width) {
        int cachedHeight = getCachedData(text.toString(), width);

        if (cachedHeight > 0) {
            return cachedHeight;
        }

        // 已绘的宽度
        float obWidth = 0;
        float obHeight = 0;

        float textSize = this.getTextSize();
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        //行高
        float lineHeight = fontMetrics.bottom - fontMetrics.top;
        //计算出的所需高度
        float height = lineSpacing;

        int leftPadding = getCompoundPaddingLeft();
        int rightPadding = getCompoundPaddingRight();

        float drawedWidth = 0;

        boolean splitFlag = false;//BackgroundColorSpan拆分用

        width = width - leftPadding - rightPadding;

        oneLineWidth = -1;

        contentList.clear();

        StringBuilder sb;

        LINE line = new LINE();


        for (int i = 0; i < obList.size(); i++) {
            if (contentList.size() <= 2) {
                Object ob = obList.get(i);

                if (ob instanceof String) {
                    obWidth = paint.measureText((String) ob);
                    obHeight = textSize;
                    if ("\n".equals(ob)) { //遇到"\n"则换行
                        obWidth = width - drawedWidth;
                    }
                } else if (ob instanceof SpanObject) {
                    Object span = ((SpanObject) ob).span;
                    if (span instanceof DynamicDrawableSpan) {
                        int start = ((Spannable) text).getSpanStart(span);
                        int end = ((Spannable) text).getSpanEnd(span);
                        obWidth = ((DynamicDrawableSpan) span).getSize(getPaint(), text, start, end, mSpanFmInt);
                        obHeight = Math.abs(mSpanFmInt.top) + Math.abs(mSpanFmInt.bottom);
                        if (obHeight > lineHeight) {
                            lineHeight = obHeight;
                        }
                    } else if (span instanceof BackgroundColorSpan) {
                        String str = ((SpanObject) ob).source.toString();
                        obWidth = paint.measureText(str);
                        obHeight = textSize;

                        //如果太长,拆分
                        int k = str.length() - 1;
                        while (width - drawedWidth < obWidth) {
                            obWidth = paint.measureText(str.substring(0, k--));
                        }
                        if (k < str.length() - 1) {
                            splitFlag = true;
                            SpanObject so1 = new SpanObject();
                            so1.start = ((SpanObject) ob).start;
                            so1.end = so1.start + k;
                            so1.source = str.substring(0, k + 1);
                            so1.span = ((SpanObject) ob).span;

                            SpanObject so2 = new SpanObject();
                            so2.start = so1.end;
                            so2.end = ((SpanObject) ob).end;
                            so2.source = str.substring(k + 1, str.length());
                            so2.span = ((SpanObject) ob).span;

                            ob = so1;
                            obList.set(i, so2);
                            i--;
                        }
                    }//做字符串处理
                    else {
                        String str = ((SpanObject) ob).source.toString();
                        obWidth = paint.measureText(str);
                        obHeight = textSize;
                    }
                }

                //这一行满了，存入contentList,新起一行
                if (width - drawedWidth < obWidth || splitFlag) {
                    splitFlag = false;
                    contentList.add(line);

                    if (drawedWidth > lineWidthMax) {
                        lineWidthMax = drawedWidth;
                    }
                    drawedWidth = 0;
                    //判断是否有分段
                    int objNum = line.line.size();
                    if (paragraphSpacing > 0
                            && objNum > 0
                            && line.line.get(objNum - 1) instanceof String
                            && "\n".equals(line.line.get(objNum - 1))) {
                        height += line.height + paragraphSpacing;
                    } else {
                        height += line.height + lineSpacing;
                    }

                    lineHeight = obHeight;

                    line = new LINE();
                }

                drawedWidth += obWidth;

                if (ob instanceof String && line.line.size() > 0 && (line.line.get(line.line.size() - 1) instanceof String)) {
                    int size = line.line.size();
                    sb = new StringBuilder();
                    sb.append(line.line.get(size - 1));
                    sb.append(ob);
                    ob = sb.toString();
                    obWidth = obWidth + line.widthList.get(size - 1);
                    line.line.set(size - 1, ob);
                    line.widthList.set(size - 1, (int) obWidth);
                    line.height = (int) lineHeight;
                } else {
                    line.line.add(ob);
                    line.widthList.add((int) obWidth);
                    line.height = (int) lineHeight;
                }
            }
        }

        if (drawedWidth > lineWidthMax) {
            lineWidthMax = drawedWidth;
        }

        if (line != null && line.line.size() > 0) {
            contentList.add(line);
            height += lineHeight + lineSpacing;
        }
        if (contentList.size() <= 1) {
            oneLineWidth = (int) drawedWidth + leftPadding + rightPadding;
            height = lineSpacing + lineHeight + lineSpacing;
        }

        cacheData(width, (int) height);
        return (int) height;
    }

    /**
     * 获取缓存的测量数据，避免多次重复测量
     *
     * @param text
     * @param width
     * @return height
     */
    @SuppressWarnings("unchecked")
    private int getCachedData(String text, int width) {
        SoftReference<MeasuredData> cache = measuredData.get(text);
        if (cache == null) {
            return -1;
        }
        MeasuredData md = cache.get();
        if (md != null && md.textSize == this.getTextSize() && width == md.width) {
            lineWidthMax = md.lineWidthMax;
            contentList = (ArrayList<LINE>) md.contentList.clone();
            oneLineWidth = md.oneLineWidth;

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < contentList.size(); i++) {
                LINE line = contentList.get(i);
                sb.append(line.toString());
            }
            return md.measuredHeight;
        } else {
            return -1;
        }
    }

    /**
     * 缓存已测量的数据
     *
     * @param width
     * @param height
     */
    @SuppressWarnings("unchecked")
    private void cacheData(int width, int height) {
        MeasuredData md = new MeasuredData();
        md.contentList = (ArrayList<LINE>) contentList.clone();
        md.textSize = this.getTextSize();
        md.lineWidthMax = lineWidthMax;
        md.oneLineWidth = oneLineWidth;
        md.measuredHeight = height;
        md.width = width;
        md.hashIndex = ++hashIndex;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < contentList.size(); i++) {
            LINE line = contentList.get(i);
            sb.append(line.toString());
        }

        SoftReference<MeasuredData> cache = new SoftReference<MeasuredData>(md);
        measuredData.put(text.toString(), cache);
    }

    /**
     * 用本函数代替{@link #setText(CharSequence)}
     *
     * @param cs
     */
    public void setMText(CharSequence cs) {
        text = cs;

        obList.clear();

        ArrayList<SpanObject> isList = new ArrayList<SpanObject>();
        useDefault = false;

        if (cs instanceof Spannable) {
            CharacterStyle[] spans = ((Spannable) cs).getSpans(0, cs.length(), CharacterStyle.class);
            for (int i = 0; i < spans.length; i++) {
                int s = ((Spannable) cs).getSpanStart(spans[i]);
                int e = ((Spannable) cs).getSpanEnd(spans[i]);
                SpanObject iS = new SpanObject();
                iS.span = spans[i];
                iS.start = s;
                iS.end = e;
                iS.source = cs.subSequence(s, e);
                isList.add(iS);
            }
        }

        //对span进行排序，以免不同种类的span位置错乱
        SpanObject[] spanArray = new SpanObject[isList.size()];
        isList.toArray(spanArray);
        Arrays.sort(spanArray, 0, spanArray.length, new SpanObjectComparator());
        isList.clear();
        for (int i = 0; i < spanArray.length; i++) {
            isList.add(spanArray[i]);
        }

        String str = cs.toString();

        for (int i = 0, j = 0; i < cs.length(); ) {
            if (j < isList.size()) {
                SpanObject is = isList.get(j);
                if (i < is.start) {
                    Integer cp = str.codePointAt(i);
                    //支持增补字符
                    if (Character.isSupplementaryCodePoint(cp)) {
                        i += 2;
                    } else {
                        i++;
                    }

                    obList.add(new String(Character.toChars(cp)));

                } else if (i >= is.start) {
                    obList.add(is);
                    j++;
                    i = is.end;
                }
            } else {
                Integer cp = str.codePointAt(i);
                if (Character.isSupplementaryCodePoint(cp)) {
                    i += 2;
                } else {
                    i++;
                }

                obList.add(new String(Character.toChars(cp)));
            }
        }

        requestLayout();
    }

    public void setUseDefault(boolean useDefault) {
        this.useDefault = useDefault;
        if (useDefault) {
            this.setText(text);
            this.setTextColor(textColor);
        }
    }

    /**
     * 设置行距
     *
     * @param lineSpacingDP 行距，单位dp
     */
    public void setLineSpacingDP(int lineSpacingDP) {
        this.lineSpacingDP = lineSpacingDP;
        lineSpacing = dip2px(context, lineSpacingDP);
    }

    public void setParagraphSpacingDP(int paragraphSpacingDP) {
        paragraphSpacing = dip2px(context, paragraphSpacingDP);
    }


    public int getLineSpacingDP() {
        return lineSpacingDP;
    }

    public void setMaxLine(int maxLine) {
        this.maxLine = maxLine;
    }


    class SpanObject {
        public Object span;
        public int start;
        public int end;
        public CharSequence source;
    }


    class SpanObjectComparator implements Comparator<SpanObject> {
        @Override
        public int compare(SpanObject lhs, SpanObject rhs) {

            return lhs.start - rhs.start;
        }
    }


    class LINE {
        public ArrayList<Object> line = new ArrayList<Object>();
        public ArrayList<Integer> widthList = new ArrayList<Integer>();
        public float height;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("height:" + height + "   ");
            for (int i = 0; i < line.size(); i++) {
                sb.append(line.get(i) + ":" + widthList.get(i));
            }
            return sb.toString();
        }
    }


    class MeasuredData {
        public int measuredHeight;
        public float textSize;
        public int width;
        public float lineWidthMax;
        public int oneLineWidth;
        public int hashIndex;
        ArrayList<LINE> contentList;
    }

}

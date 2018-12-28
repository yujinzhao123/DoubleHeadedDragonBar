package cn.bar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

public class DoubleHeadedDragonBar extends View {


    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams, mLayoutParams1, mLayoutParams2;

    DhdBarCallBack callBack;

    public void setCallBack(DhdBarCallBack callBack) {
        this.callBack = callBack;
    }

    float buttonWidth = 0;//按钮宽
    float buttonHeight = 0;//按钮高
    private int viewWidth;//控件宽
    Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    int textColor, bgColor;
    String unitStr1 = "";
    String unitStr2 = "";
    float unitTextSize = 0;// 显示单位的字体大小

    Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint valuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int bgHeight = dp2px(4);//进度条宽度

    float seekWidth;//进度条宽

    Bitmap buttonImg;

    /**
     * 百分比
     */
    int max = 100;
    //最小值的百分比
    private int minValue = 0;
    //最大值的百分比
    private int maxValue = 100;

    boolean isMinMode = true;//选择最小值模式

    private View toastView, toastView1, toastView2;


    public void setUnit(String unitStr1, String unitStr2) {
        this.unitStr1 = unitStr1;
        this.unitStr2 = unitStr2;
        invalidate();
    }

    public int getMinValue() {
        return minValue;
    }

    /**
     * 设置最小值的百分之多少
     *
     * @param minValue
     */
    public void setMinValue(int minValue) {
        if (minValue < 0) {
            minValue = 0;
        } else if (minValue > max) {
            minValue = max;
        }

        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    /**
     * 设置最大值的百分之多少
     *
     * @param maxValue
     */
    public void setMaxValue(int maxValue) {
        if (maxValue < 0) {
            maxValue = 0;
        } else if (maxValue > max) {
            maxValue = max;
        }
        this.maxValue = maxValue;
    }

    public DoubleHeadedDragonBar(Context context) {
        this(context, null);
    }

    public DoubleHeadedDragonBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleHeadedDragonBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DoubleHeadedDragonBar, defStyleAttr, 0);
        buttonHeight = a.getDimension(R.styleable.DoubleHeadedDragonBar_button_height, dp2px(30));
        buttonWidth = a.getDimension(R.styleable.DoubleHeadedDragonBar_button_width, dp2px(60));
        textColor = a.getColor(R.styleable.DoubleHeadedDragonBar_text_color, Color.parseColor("#5C6980"));
        bgColor = a.getColor(R.styleable.DoubleHeadedDragonBar_bg_color, Color.parseColor("#F2F4FE"));

        bgHeight = (int) a.getDimension(R.styleable.DoubleHeadedDragonBar_seek_height, dp2px(4));

        int valueColor = a.getColor(R.styleable.DoubleHeadedDragonBar_value_color, Color.parseColor("#1B97F7"));
        valuePaint.setColor(valueColor);
        int buttonImgId = a.getResourceId(R.styleable.DoubleHeadedDragonBar_button_img, R.drawable.button);
        a.recycle();


        //设置单位显示额的字体
        unitTextSize = buttonHeight * 0.4f;
        textPaint.setTextSize(unitTextSize);
        textPaint.setColor(textColor);

        bgPaint.setColor(bgColor);

        buttonImg = setImgSize(BitmapFactory.decodeResource(context.getResources(), buttonImgId), buttonWidth, buttonHeight);

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        mLayoutParams2 = new WindowManager.LayoutParams();
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams1 = new WindowManager.LayoutParams();
        initLayoutParams(mLayoutParams2);
        initLayoutParams(mLayoutParams);
        initLayoutParams(mLayoutParams1);


    }


    private void initLayoutParams(WindowManager.LayoutParams mLayoutParams) {

        mLayoutParams.gravity = Gravity.START | Gravity.TOP;
        mLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        // MIUI禁止了开发者使用TYPE_TOAST，Android 7.1.1 对TYPE_TOAST的使用更严格
        if (BubbleUtils.isMIUI() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }

    }

    public Bitmap setImgSize(Bitmap bm, float newWidth, float newHeight) {
        // 获得图片的宽高.
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例.
        float scaleWidth = newWidth / width;
        float scaleHeight = newHeight / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public void setToastView(View view) {
        toastView = view;
    }

    public void setToastView1(View view) {
        toastView1 = view;
    }

    public void setToastView2(View view) {
        toastView2 = view;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawUnit(canvas);
        drawBg(canvas);
        drawValue(canvas);
        drawButton(canvas);
        calculationToastIndex();
        drawBubble();
        showToastView();
    }

    public void drawBubble() {

        drawToast();
        drawToast1();
        if (toastView2 == null) {
            return;
        }

        if (isCoincide) {
//            if (toastView2.getVisibility() == GONE) {
//            }
            mLayoutParams2.x = (toastViewX + toastView1X) / 2;
            mLayoutParams2.y = toastViewY;
            if (toastView2.getParent() == null) {
                mWindowManager.addView(toastView2, mLayoutParams2);
            } else {
                mWindowManager.updateViewLayout(toastView2, mLayoutParams2);
            }
            toastView2.setVisibility(VISIBLE);

        } else {
            toastView2.setVisibility(GONE);
        }


    }

    //弹出控件的x,y坐标
    int toastViewX = 0;
    int toastViewY = 0;
    int[] point = new int[2];

    int toastView1X = 0;
    int toastView1Y = 0;

    private void drawToast() {

        if (toastView == null) {
            return;
        }

        if (isCoincide) {
            toastView.setVisibility(GONE);
            return;
        }

        if (toastView.getVisibility() == GONE) {
            toastView.setVisibility(VISIBLE);
        }
        mLayoutParams.x = toastViewX;
        mLayoutParams.y = toastViewY;
        if (toastView.getParent() == null) {

            mWindowManager.addView(toastView, mLayoutParams);
            return;
        } else {

            mWindowManager.updateViewLayout(toastView, mLayoutParams);
        }

    }

    private void drawToast1() {

        if (toastView1 == null) {
            return;
        }
        if (isCoincide) {
            toastView1.setVisibility(GONE);
            return;
        }

        if (toastView1.getVisibility() == GONE) {
            toastView1.setVisibility(VISIBLE);
        }
        mLayoutParams1.x = toastView1X;
        mLayoutParams1.y = toastView1Y;
        if (toastView1.getParent() == null) {
            mWindowManager.addView(toastView1, mLayoutParams1);
            return;
        } else {
            mWindowManager.updateViewLayout(toastView1, mLayoutParams1);
        }

    }


    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatus_bar_height() {
        int result = 0;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 气泡是否重合
     */
    boolean isCoincide = false;

    /**
     * 计算toast坐标
     */
    private void calculationToastIndex() {

        int width = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);

        int height = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);

        if (toastView != null) {
            toastView.measure(width, height);
            toastViewY = (int) (point[1] - getStatus_bar_height() - (toastView.getMeasuredHeight() - buttonHeight / 2));
            toastViewX = (int) (buttonWidth / 2 + seekWidth * minValue / max + point[0] - toastView.getMeasuredWidth() / 2);
        }


        if (toastView1 != null) {
            toastView1.measure(width, height);
            toastView1Y = (int) (point[1] - getStatus_bar_height() - (toastView1.getMeasuredHeight() - buttonHeight / 2));
            toastView1X = (int) (buttonWidth / 2 + seekWidth * maxValue / max + point[0] - toastView1.getMeasuredWidth() / 2);
        }


        if (toastView != null && toastView1 != null) {
            if (Math.abs(toastViewX - toastView1X) < (toastView.getMeasuredWidth() / 2 + toastView1.getMeasuredWidth() / 2)) {
                isCoincide = true;
            } else {
                isCoincide = false;
            }
        }


    }


    private void drawValue(Canvas canvas) {

        float minx = seekWidth * minValue / max;
        float m1 = minx + buttonWidth / 2;

        float maxx = seekWidth * maxValue / max;
        float m2 = maxx + buttonWidth / 2;

        canvas.drawRoundRect(new RectF(m1, buttonHeight - bgHeight / 2, m2, buttonHeight + bgHeight / 2),
                bgHeight / 2,
                bgHeight / 2
                , valuePaint);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                performClick();
                getParent().requestDisallowInterceptTouchEvent(true);
                if (!isTouchSeek(event)) {
                    return false;
                }

                break;

            case MotionEvent.ACTION_MOVE:
                int a = minValue;
                int b = maxValue;
                getTouchSeekValue(event);
                if (a == minValue && b == maxValue) {

                } else {
                    invalidate();
                }


                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if (callBack != null) {
                    callBack.onEndTouch(minValue, maxValue);
                }
//
                break;
        }

        return true;
    }

    private void showToastView() {
        if (callBack != null) {
            if (toastView != null && toastView.getParent() != null) {
                ((TextView) toastView).setText(callBack.getMinString(minValue));
            }

            if (toastView1 != null && toastView1.getParent() != null) {
                ((TextView) toastView1).setText(callBack.getMaxString(maxValue));
            }

            if (toastView2 != null && toastView2.getParent() != null) {
                ((TextView) toastView2).setText(callBack.getMinMaxString(minValue, maxValue));
            }
        }
    }

    private void getTouchSeekValue(MotionEvent event) {
        int x = (int) event.getX();
        x = (int) (x - buttonWidth / 2);
        int t = (int) (max * x / seekWidth);
        if (isMinMode) {

            if (t < 0) {
                minValue = 0;
            } else if (t > maxValue) {
                minValue = maxValue;
            } else {
                minValue = t;
            }

        } else {
            if (t < minValue) {
                maxValue = minValue;
            } else if (t > max) {
                maxValue = max;
            } else {
                maxValue = t;
            }
        }
    }


    /**
     * 时候触摸在控件内
     * 同时判断是拖动最大按钮还是最小按钮
     *
     * @param event
     * @return
     */
    private boolean isTouchSeek(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (y < buttonHeight / 2) {
            return false;
        }

        float minx = seekWidth * minValue / max;
        float m1 = minx + buttonWidth / 2;

        float maxx = seekWidth * maxValue / max;
        float m2 = maxx + buttonWidth / 2;

        if (isMinMode) {

            if (x > m1 - buttonWidth / 2 && x < m1 + buttonWidth / 2) {
                isMinMode = true;
                return true;
            }

            if (x > m2 - buttonWidth / 2 && x < m2 + buttonWidth) {
                isMinMode = false;
                return true;
            }
        } else {
            if (x > m2 - buttonWidth / 2 && x < m2 + buttonWidth) {
                isMinMode = false;
                return true;
            }

            if (x > m1 - buttonWidth / 2 && x < m1 + buttonWidth / 2) {
                isMinMode = true;
                return true;
            }
        }

        return false;
    }

    private void drawButton(Canvas canvas) {
        if (isMinMode) {
            canvas.drawBitmap(buttonImg, seekWidth * maxValue / max, buttonHeight / 2, textPaint);
            canvas.drawBitmap(buttonImg, seekWidth * minValue / max, buttonHeight / 2, textPaint);
        } else {
            canvas.drawBitmap(buttonImg, seekWidth * minValue / max, buttonHeight / 2, textPaint);
            canvas.drawBitmap(buttonImg, seekWidth * maxValue / max, buttonHeight / 2, textPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        seekWidth = viewWidth - buttonWidth;
        getLocationOnScreen(point);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {


        if (visibility != VISIBLE) {
            hideToastView(toastView);
            hideToastView(toastView1);
            hideToastView(toastView2);
        } else {

        }
        super.onVisibilityChanged(changedView, visibility);
    }

    private void hideToastView(View view) {
        if (view == null)
            return;

        view.setVisibility(GONE); // 防闪烁
        if (view.getParent() != null) {
            mWindowManager.removeViewImmediate(view);
        }
    }

    private void drawBg(Canvas canvas) {

        canvas.drawRoundRect(new RectF(buttonWidth / 2, buttonHeight - bgHeight / 2, viewWidth - buttonWidth / 2, buttonHeight + bgHeight / 2),
                bgHeight / 2,
                bgHeight / 2
                , bgPaint);
    }

    private void drawUnit(Canvas canvas) {

        drawText(canvas, buttonWidth / 2, unitTextSize, unitStr1);
        drawText(canvas, viewWidth - buttonWidth / 2, unitTextSize, unitStr2);

    }


    private void drawText(Canvas canvas, float x, float y, String str) {
        //居中对齐
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(str, x, y, textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defalueSize = 200;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int specValue = MeasureSpec.getSize(widthMeasureSpec);
        viewWidth = specValue;
        switch (mode) {
            //指定一个默认值
            case MeasureSpec.UNSPECIFIED:
                viewWidth = defalueSize;
                break;
            //取测量值
            case MeasureSpec.EXACTLY:
                viewWidth = specValue;
                break;
            //取测量值和默认值中的最小值
            case MeasureSpec.AT_MOST:
                viewWidth = Math.min(defalueSize, specValue);
                break;
            default:
                break;
        }
        setMeasuredDimension(viewWidth, (int) (buttonHeight * 1.5f));

    }

    int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    public abstract static class DhdBarCallBack {

        public String getMinString(int value) {
            return value + "";
        }

        public String getMaxString(int value) {
            return value + "";
        }

        public String getMinMaxString(int value, int value1) {
            return value + "";
        }

        public void onEndTouch(float minPercentage, float maxPercentage) {

        }
    }
}

package com.example.xin.friendlyreminder.customlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.xin.friendlyreminder.R;

/**
 * Created by xin on 2017/11/30.
 * 右侧导航栏的组件
 */

public class SideBarView extends View {
    int viewHeight;/**用于解决滑动超出组件高度崩溃问题*/
    public static String[] b = { "↑","A", "B", "C", "D", "E", "F", "G", "H", "I",
                    "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                    "W", "X", "Y", "Z", "#" };
    private int selectPos = -1;

    private final int defaultNormalColor = Color.TRANSPARENT;
    private final int defaultPressColor = Color.parseColor("#1F000000");//滑动或点击时组件的背景颜色
    private final int defaultTextSize = 30;//导航栏字母的大小
    private final int defaultNorTextColor = Color.parseColor("#cc181818");//导航栏默认状态下字母颜色
    private final int defaultPressTextColor = Color.parseColor("#ff000000");//滑动或点击时字母的颜色


    private int sideBarBgNorColor;//默认状态下背景颜色
    private int sideBarBgPressColor;//点击时背景颜色
    private int sideBarTextSize;//导航栏字母大小
    private int sideBarNorTextColor;//默认状态下字母颜色
    private int sideBarPressTextColor;//点击时字母颜色


    public SideBarView(Context context) {
        this(context, null);
    }

    public SideBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public SideBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SideBarView, defStyleAttr, 0);
        sideBarBgNorColor = typedArray.getColor(R.styleable.SideBarView_sidebar_nor_background,defaultNormalColor);
        sideBarBgPressColor = typedArray.getColor(R.styleable.SideBarView_sidebar_press_background,defaultPressColor);
        sideBarTextSize = typedArray.getInt(R.styleable.SideBarView_sidebar_text_size, defaultTextSize);
        sideBarNorTextColor = typedArray.getColor(R.styleable.SideBarView_sidebar_text_color_nor, defaultNorTextColor);
        sideBarPressTextColor = typedArray.getColor(R.styleable.SideBarView_sidebar_text_color_press, defaultPressTextColor);

        typedArray.recycle();

        init();
    }

    Paint paint;
    Paint paintSelect;
    private void init() {
        paint= new Paint() ;
        paint.setAntiAlias(true);
        paint.setColor(sideBarNorTextColor);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextSize(sideBarTextSize);
        paintSelect= new Paint();
        paintSelect.setAntiAlias(true);
        paintSelect.setTypeface(Typeface.DEFAULT_BOLD);
        paintSelect.setTextSize(sideBarTextSize);
        paintSelect.setColor(sideBarPressTextColor);

    }
    int height;
    int width;
    int perHeight;

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        float x = event.getY();
        /**应该把高度限定在此组件的最大高度里*/

        //Log.i("现在滑动深度：",x+"");
        if (x < 0){
            x = 0;
        }
        if (x>viewHeight){
            x = viewHeight;
        }
        //Log.i("但我觉得",x+"就够了");
        int position = (int) (x / perHeight);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(sideBarBgPressColor);
                selectPos = position;
                if(listener != null)
                    listener.onLetterSelected(b[selectPos]);
                invalidate();
                break;


            case MotionEvent.ACTION_MOVE:
                if(position != selectPos){
                    //切换到其他字母
                    selectPos = position;
                    if(listener != null)
                        listener.onLetterChanged(b[selectPos]);
                    invalidate();
                }


                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setBackgroundColor(sideBarBgNorColor);
                if(listener != null){
                    listener.onLetterReleased(b[selectPos]);
                }
                break;
        }



        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        height = getHeight();
        width = getWidth();
        perHeight = height / b.length;
        for (int i = 0; i < b.length; i++) {
            canvas.drawText(b[i],width/2 - paint.measureText(b[i])/2,perHeight * i+perHeight,paint);
            if(selectPos == i){
                canvas.drawText(b[i],width/2 - paint.measureText(b[i])/2,perHeight * i+perHeight,paintSelect);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = resolveMeasure(widthMeasureSpec, true);
        int height = resolveMeasure(heightMeasureSpec,false);
        int eachHeight = height/b.length;//计算出每个字母所占高度
        //Log.i("每个字母高度：",eachHeight+"");
        viewHeight = height-eachHeight;//将最大高度减去一个字母高度，确保不会因为下滑到底部而崩溃
        //Log.i("组件高度",viewHeight+"");
        setMeasuredDimension(width,height);
    }

    private int resolveMeasure(int measureSpec ,boolean isWidth) {

        int result = 0 ;
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();

        // 获取宽度测量规格中的mode
        int mode = MeasureSpec.getMode(measureSpec);

        // 获取宽度测量规格中的size
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode){
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                float textWidth = paint.measureText(b[0]);
                if(isWidth){
                    result = getSuggestedMinimumWidth() > textWidth ? getSuggestedMinimumWidth() : (int) textWidth;
                    result += padding;
                    result = Math.min(result,size);
                }else{
                    result = size;
                    result = Math.max(result,size);
                }

                break;
        }


        return result;
    }


    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) dp2px(25);
    }


    public interface LetterSelectListener{
        void onLetterSelected(String letter);
        void onLetterChanged(String letter);
        void onLetterReleased(String letter);
    }

    private LetterSelectListener listener;
    public void setOnLetterSelectListen(LetterSelectListener listen){
        this.listener = listen;
    }

}
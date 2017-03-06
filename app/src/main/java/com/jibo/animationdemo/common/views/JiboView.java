package com.jibo.animationdemo.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by acer on 2016/11/4.
 */

public class JiboView extends ImageView {

    private int mLastX;
    private int mLastY;

    public JiboView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public JiboView(Context context) {
        super(context);

    }

    public JiboView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public boolean onTouchEvent(MotionEvent event) {
        // 获取手指按下的坐标
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                // 获取手指移动到了哪个点的坐标
                int deltaX=x-mLastX;
                int deltaY=y-mLastY;
                int translationX = (int) ViewHelper.getTranslationX(this) + deltaX;
                int translationY = (int) ViewHelper.getTranslationY(this) + deltaY;

                ViewHelper.setTranslationX(this,translationX);
                ViewHelper.setTranslationY(this,translationY);

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        mLastX=x;
        mLastY=y;
        return true;
    }

  }

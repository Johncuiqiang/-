package com.jibo.animationdemo.floatview;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.jibo.animationdemo.utils.UIUtils;

/**
 * @Author : cuiqiang
 * @DATE : 2019/4/17 15:45
 * @Description :
 */
public class ViewGestureHelper {

    private int lastx = 0;
    private int lasty = 0;
    private int movex = 0;
    private int movey = 0;
    private boolean isMove;
    private View mFloatView;
    private int width;
    private int height;
    private WindowManager.LayoutParams mWindowManagerParams;
    private WindowManager mWindowManager;
    private RelativeLayout mFloatLayout;

    public ViewGestureHelper(Context context, WindowManager.LayoutParams mWindowManagerParams,
                             RelativeLayout floatLayout) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = (int) UIUtils.getScreenWidth();
        height = (int) UIUtils.getScreenHeight();
        this.mWindowManagerParams = mWindowManagerParams;
        this.mFloatLayout = floatLayout;
    }

    public void setViewEvent(View view) {
        if (view == null || mWindowManagerParams == null || mFloatLayout == null || mWindowManager == null) {
            return;
        }
        mFloatView = view;
        view.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastx = (int) event.getRawX();
                        lasty = (int) event.getRawY();
                        isMove = false;
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        int curx = (int) event.getRawX();
                        int cury = (int) event.getRawY();
                        int x = Math.abs(curx - lastx);
                        int y = Math.abs(cury - lasty);
                        //限制一定的灵敏度，交互会更合理一些
                        if (x < 5 || y < 5) {
                            isMove = false;
                            return false;
                        } else {
                            isMove = true;
                        }

                        mWindowManagerParams.x = curx - mFloatView.getMeasuredWidth() / 2;
                        // TODO-cuiqiang01: 2019/4/18 减去状态栏的高度
                        mWindowManagerParams.y = cury - mFloatView.getMeasuredHeight() / 2;
                        // 刷新
                        mWindowManager.updateViewLayout(mFloatLayout, mWindowManagerParams);
                        return true;
                    case MotionEvent.ACTION_UP:
                        int finalX = (int) event.getRawX();
                        int finalY = (int) event.getRawY();
                        //是否需要吸附边缘
                        boolean isBindEdgeX = false;
                        //y轴的吸附上边
                        if (finalY < mFloatView.getMeasuredHeight()) {
                            movey = 0;
                            movex = finalX - mFloatView.getMeasuredWidth() / 2;
                        }
                        //y轴的吸附下边
                        if (finalY > height - mFloatView.getMeasuredHeight()) {
                            movey = height - mFloatView.getMeasuredHeight();
                            movex = finalX - mFloatView.getMeasuredWidth() / 2;
                        }
                        //位于中间这个时候需要判断左右吸附
                        if (finalY > mFloatView.getMeasuredHeight() && finalY < height - mFloatView.getMeasuredHeight()) {
                            isBindEdgeX = true;
                        }
                        //x轴的吸附左边
                        if (isBindEdgeX && finalX - mFloatView.getMeasuredWidth() / 2 < width / 2) {
                            movex = 0;
                            movey = finalY - mFloatView.getMeasuredHeight() / 2;
                        //吸附右边
                        } else if (isBindEdgeX && finalX - mFloatView.getMeasuredWidth() / 2 > width / 2) {
                            movex = width - mFloatView.getMeasuredWidth();
                            movey = finalY - mFloatView.getMeasuredHeight() / 2;
                        }
                        mWindowManagerParams.x = movex;
                        mWindowManagerParams.y = movey;
                        if (isMove) {
                            mWindowManager.updateViewLayout(mFloatLayout, mWindowManagerParams);
                        }
                        return isMove;//false 为点击 true 为移动
                    default:
                        break;
                }
                return false;
            }
        });
    }
}

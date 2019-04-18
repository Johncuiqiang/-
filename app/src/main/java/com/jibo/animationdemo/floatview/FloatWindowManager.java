package com.jibo.animationdemo.floatview;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * @Author : cuiqiang
 * @DATE : 2019/4/17 15:26
 * @Description :
 */
public class FloatWindowManager {

    private volatile static FloatWindowManager INSTANCE;
    private FloatWindowHelper mFloatWindowHelper;

    public static FloatWindowManager getInstance(){
        if(INSTANCE == null){
            synchronized (FloatWindowManager.class){
                if(INSTANCE == null){
                    INSTANCE = new FloatWindowManager();
                }
            }
        }
        return INSTANCE;
    }

    public void init(Context context){
        mFloatWindowHelper = new FloatWindowHelper();
        WindowManager.LayoutParams windowManagerParams = mFloatWindowHelper.createWindowManagerParams(context);
        RelativeLayout floatLayout = mFloatWindowHelper.createFloatLayout(context);
        View floatView = mFloatWindowHelper.createFloatView();
        //手势绑定
        ViewGestureHelper viewGestureHelper = new ViewGestureHelper(context,windowManagerParams,floatLayout);
        viewGestureHelper.setViewEvent(floatView);
    }

    /**
     * 只要大类页destroy就关闭
     */
    public void release(){
        if (mFloatWindowHelper != null){
            mFloatWindowHelper.release();
        }
    }

}

package com.jibo.animationdemo.floatview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.jibo.animationdemo.R;
import com.jibo.animationdemo.utils.UIUtils;


/**
 * @Author : cuiqiang
 * @DATE : 2019/4/17 15:35
 * @Description :
 */
public class FloatWindowHelper {

    private WindowManager.LayoutParams mWindowManagerParams;
    private WindowManager mWindowManager;
    private ImageButton mFloatView;
    private RelativeLayout mFloatLayout;

    /**
     * 创建窗口
     */
    public WindowManager.LayoutParams createWindowManagerParams(Context context){
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mWindowManagerParams = new WindowManager.LayoutParams();
        //设置window type  
        mWindowManagerParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        //设置图片格式，效果为背景透明  
        mWindowManagerParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）  
        mWindowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶  
        mWindowManagerParams.gravity = Gravity.LEFT | Gravity.TOP;
        //以屏幕左上角为原点，设置x、y初始值，相对于gravity  
        mWindowManagerParams.x = (int)UIUtils.getScreenWidth() - 200;
        // TODO-cuiqiang01: 2019/4/17 需要减去状态栏高度
        mWindowManagerParams.y = (int)UIUtils.getScreenHeight() - 400;
        //设置悬浮窗口长宽数据    
        mWindowManagerParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        return mWindowManagerParams;
    }

    /**
     * 创建悬浮窗控件
     */
    public View createFloatView(){
        if (mWindowManagerParams == null || mWindowManager == null || mFloatLayout == null){
            return null;
        }
        mWindowManager.addView(mFloatLayout, mWindowManagerParams);//浮动窗口按钮  
        mFloatView = (ImageButton) mFloatLayout.findViewById(R.id.ib_jibo);
        return mFloatView;
    }

    /**
     * 创建悬浮窗布局
     */
    public RelativeLayout createFloatLayout(Context context){
        if (mWindowManagerParams == null){
            return null;
        }
        LayoutInflater inflater = LayoutInflater.from(context);//获取浮动窗口视图所在布局  
        mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.act_move_pic,null);
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        return mFloatLayout;
    }

    /**
     * 移除悬浮窗
     */
    public void release(){
        if (mFloatLayout != null) {//移除悬浮窗口  
            mWindowManager.removeView(mFloatLayout);
        }
    }


}

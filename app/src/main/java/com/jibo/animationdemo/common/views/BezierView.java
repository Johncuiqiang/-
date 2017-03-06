package com.jibo.animationdemo.common.views;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import com.jibo.animationdemo.common.MyPoint;
import com.jibo.animationdemo.bean.PointsBean;
import com.jibo.animationdemo.utils.UIUtils;
import java.util.ArrayList;

/**
 * Created by acer on 2016/11/6.
 */

public class BezierView extends View {

    //移动的最大范围
    private static int LEFT_SCROLL_EDGE;
    private static int RIGHT_SCROLL_EDGE;
    private static int UP_SCROLL_BODY_DEGE;
    private static int DOWN_SCROLL_BODY_DEGE;
    private static int UP_SCROLL_EYE;
    private static int DOWN_SCROLL_EYE;

    //贝塞尔曲线相关参数
    private MyPoint mStartPointLeft;
    private MyPoint mStartPointRight;
    private MyPoint mAssistPointLeft;
    private MyPoint mAssistPointRight;
    private MyPoint mEndPointLeft;
    private MyPoint mEndPointRight;
    //头部，眼部，鼻部相关参数
    private MyPoint mCirclePoint;
    private MyPoint mLeftEyePoint;
    private MyPoint mRightEyePoint;
    private MyPoint mLeftEyeBallPoint;
    private MyPoint mRightEyeBallPoint;
    private MyPoint mNosePoint;

    private String TAG = "pppp";
    private Paint mPaint;
    private Path mPath;
    private DecelerateInterpolator mInterpolator;
    private PointsBean bean;
    private ArrayList<MyPoint> mResetList;//需要重置的点的集合

    private int mBodyColor = 0xffffaa22;
    private int lastY = 0;
    private int moveY;
    private int moveEyeX;
    private int moveEyeY;
    private int lastEyeX = 0;
    private int lastEyeY = 0;

    //针对body手指移动总距离
    private float allDy = 0;
    //手指移动总距离
    private float allEyeDx = 0;
    private float allEyeDy = 0;
    private float mScreenWidth;
    private float mScreenHeight;
    private float mCircleR;
    //滑动限制参数（原路手指移动5，图像移动1）
    private float mBodyRatio;
    private float mEyeRatio;

    //重置动画是否执行完成
    private boolean isResetFinish;
    //是否停止重置动画
    private boolean isAbortAnim;
    //只进行点击的话不执行动画
    private boolean isPointerMove;

    public BezierView(Context context) {
        this(context, null);
    }

    public BezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mInterpolator = new DecelerateInterpolator(0.8f);
        mPaint = new Paint();
        mPath = new Path();

        mScreenWidth = UIUtils.getScreenWidth();
        mScreenHeight = UIUtils.getScreenHeight();
        initPoint(mScreenWidth, mScreenHeight);
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 防抖动
        mPaint.setDither(true);

    }

    private void initPoint(float mScreenWidth, float mScreenHeight) {

        bean = new PointsBean(mScreenWidth, mScreenHeight);
        mStartPointLeft = bean.getStartPointLeft();
        mStartPointRight = bean.getStartPointRight();
        mAssistPointLeft = bean.getAssistPointLeft();
        mAssistPointRight = bean.getAssistPointRight();
        mEndPointLeft = bean.getEndPointLeft();
        mEndPointRight = bean.getEndPointRight();
        mCirclePoint = bean.getCirclePoint();
        mCircleR = bean.getCircleR();
        mLeftEyePoint = bean.getLeftEyePoint();
        mRightEyePoint = bean.getRightEyePoint();
        mLeftEyeBallPoint = bean.getLeftEyeBallPoint();
        mRightEyeBallPoint = bean.getRightEyeBallPoint();
        mNosePoint = bean.getNosePoint();

        mResetList = new ArrayList<>();
        mResetList.add(mStartPointLeft);
        mResetList.add(mAssistPointLeft);
        mResetList.add(mCirclePoint);
        mResetList.add(mLeftEyePoint);
        mResetList.add(mRightEyePoint);
        mResetList.add(mLeftEyeBallPoint);
        mResetList.add(mRightEyeBallPoint);
        mResetList.add(mNosePoint);
        initSrollParams(bean);
    }

    private void initSrollParams(PointsBean bean) {
        UP_SCROLL_BODY_DEGE = bean.getUpScrollEdge();
        DOWN_SCROLL_BODY_DEGE = bean.getDownScrollEdge();
        LEFT_SCROLL_EDGE = bean.getLeftScrollEdge();
        RIGHT_SCROLL_EDGE = bean.getRightScrollEdge();
        UP_SCROLL_EYE = bean.getUpScrollEye();
        DOWN_SCROLL_EYE = bean.getDownScrollEye();
        mBodyRatio = bean.getBodyRatio();
        mEyeRatio = bean.getEyeRatio();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mBodyColor);
        mPaint.setStrokeWidth(UIUtils.dp2px(5));
        mPaint.setStyle(Paint.Style.FILL);

        drawHead(canvas);
        drawBody(canvas);
        drawEye(canvas);
        drawNose(canvas);
    }

    //画头部
    private void drawHead(Canvas canvas) {
        canvas.drawCircle(mCirclePoint.x, mCirclePoint.y, mCircleR, mPaint);
    }

    private void drawBody(Canvas canvas) {
        // 重置路径
        mPath.reset();
        // 起点
        mPath.moveTo(mStartPointLeft.x, mStartPointLeft.y);
        mPath.quadTo(mAssistPointLeft.x, mAssistPointLeft.y, mEndPointLeft.x, mEndPointLeft.y);
        mPath.lineTo(mEndPointRight.x, mEndPointRight.y);
        mPath.quadTo(mAssistPointRight.x, mAssistPointLeft.y, mStartPointRight.x, mStartPointLeft.y);
        mPath.lineTo(mStartPointLeft.x, mStartPointLeft.y);
        // 画路径
        canvas.drawPath(mPath, mPaint);
    }

    private void drawEye(Canvas canvas) {
        //绘制外眼眶
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(UIUtils.dp2px(3));
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mLeftEyePoint.x, mLeftEyePoint.y, mCircleR / 6, mPaint);
        canvas.drawCircle(mRightEyePoint.x, mRightEyePoint.y, mCircleR / 6, mPaint);
        //绘制白眼球
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mLeftEyePoint.x, mLeftEyePoint.y, mCircleR / 6, mPaint);
        canvas.drawCircle(mRightEyePoint.x, mRightEyePoint.y, mCircleR / 6, mPaint);
        //绘制黑眼球
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mLeftEyeBallPoint.x, mLeftEyeBallPoint.y, mCircleR / 12, mPaint);
        canvas.drawCircle(mRightEyeBallPoint.x, mLeftEyeBallPoint.y, mCircleR / 12, mPaint);
        //绘制瞳孔（白色）
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mLeftEyeBallPoint.x, mLeftEyeBallPoint.y - mCircleR / 24, mCircleR / 30, mPaint);
        canvas.drawCircle(mRightEyeBallPoint.x, mRightEyeBallPoint.y - mCircleR / 24, mCircleR / 30, mPaint);
    }

    private void drawNose(Canvas canvas) {
        //绘制鼻子
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mNosePoint.x, mNosePoint.y, mCircleR / 6, mPaint);
        //绘制鼻子的光照效果
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mNosePoint.x - mCircleR / 20, mNosePoint.y - mCircleR / 6 + mCircleR / 10, mCircleR / 20, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int downY = (int) event.getRawY();
        int downX = (int) event.getRawX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = downY;
                lastEyeX = downX;
                lastEyeY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG," ACTION_MOVE");
                if (!isResetFinish) {
                    //重置动画如果没有执行完，停止动画
                    Log.d(TAG," isResetFinish");
                    //当手指滑动了两次，停止第一次滑动的效果，否则会出现划不动的情况
                    isAbortAnim = true;
                    //判断动画是否执行完，让isAbortAnim为false或true
                    isResetFinish = true;
                }
                //判断是否是点击还是移动
                isPointerMove = true;
                moveBody(event);
                moveFace(event);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                allDy = 0;
                allEyeDx = 0;
                allEyeDy = 0;
                Log.d(TAG," ACTION_UP");
                //解开动画禁制
                isAbortAnim = false;
                if(isPointerMove) {
                    reset(getValuesHolder());
                }
                break;
        }
        return true;
    }

    private void moveBody(MotionEvent event) {
        moveY = (int) event.getRawY();
        int dy = moveY - lastY;
        allDy = allDy + dy;

        float allDiff = getAllDiff(allDy, mBodyRatio, UP_SCROLL_BODY_DEGE, DOWN_SCROLL_BODY_DEGE);
        //更改各点y坐标
        mStartPointLeft.y = (int) (mStartPointLeft.oy + allDiff);
        mAssistPointLeft.y = (int) (mAssistPointLeft.oy + allDiff / 2);
        mCirclePoint.y = (int) (mCirclePoint.oy + allDiff);

        lastY = moveY;
    }

    private void moveFace(MotionEvent event) {
        moveEyeX = (int) event.getRawX();
        moveEyeY = (int) event.getRawY();

        int dx = moveEyeX - lastEyeX;
        allEyeDx = allEyeDx + dx;

        int dy = moveEyeY - lastEyeY;
        allEyeDy = allEyeDy + dy;

        float allEyeDiffX = getAllDiff(allEyeDx, mEyeRatio, LEFT_SCROLL_EDGE, RIGHT_SCROLL_EDGE);
        float allEyeDiffY = getAllDiff(allEyeDy, mEyeRatio, UP_SCROLL_EYE, DOWN_SCROLL_EYE);
        float allEyeBallDiffX = allEyeDiffX * (LEFT_SCROLL_EDGE + mCircleR / 12) / LEFT_SCROLL_EDGE;//mCircleR / 12是半径差
        float allEyeBallDiffY = allEyeDiffY * (DOWN_SCROLL_EYE + mCircleR / 12) / DOWN_SCROLL_EYE;

        //更改各点坐标
        //眼眶
        mLeftEyePoint.x = (int) (mLeftEyePoint.ox + allEyeDiffX);
        mLeftEyePoint.y = (int) (mLeftEyePoint.oy + allEyeDiffY);
        mRightEyePoint.x = (int) (mRightEyePoint.ox + allEyeDiffX);
        mRightEyePoint.y = (int) (mRightEyePoint.oy + allEyeDiffY);
        //眼球
        mLeftEyeBallPoint.x = (int) (mLeftEyeBallPoint.ox + allEyeBallDiffX);
        mLeftEyeBallPoint.y = (int) (mLeftEyeBallPoint.oy + allEyeBallDiffY);
        mRightEyeBallPoint.x = (int) (mRightEyeBallPoint.ox + allEyeBallDiffX);
        mRightEyeBallPoint.y = (int) (mRightEyeBallPoint.oy + allEyeBallDiffY);
        //鼻子
        mNosePoint.x = (int) (mNosePoint.ox + allEyeDiffX);
        mNosePoint.y = (int) (mNosePoint.oy + allEyeDiffY);

        lastEyeX = moveEyeX;
        lastEyeY = moveEyeY;
    }

    private float getAllDiff(float allDistance, float ratio, int leftUpEdge, int rightDownEdge) {
        //限定边界
        if (allDistance < -ratio * leftUpEdge) {
            allDistance = -ratio * leftUpEdge;
        }
        if (allDistance > ratio * rightDownEdge) {
            allDistance = ratio * rightDownEdge;
        }

        //计算图像y轴更改值 allDiff
        float fraction;
        float allDiff;

        if (allDistance >= 0) {
            //下滑或右滑
            fraction = 1f * allDistance / (ratio * rightDownEdge);
            float interpolation = mInterpolator.getInterpolation(fraction);
            allDiff = rightDownEdge * interpolation;
        } else {
            fraction = 1f * Math.abs(allDistance) / (ratio * leftUpEdge);
            float interpolation = mInterpolator.getInterpolation(fraction);
            allDiff = -leftUpEdge * interpolation;
        }
        return allDiff;
    }

    //因为参数比较多，所以把原始参数和变化后的参数，存起来传到执行动画中
    private PropertyValuesHolder[] getValuesHolder() {

        PropertyValuesHolder[] holders = new PropertyValuesHolder[mResetList.size() + mResetList.size()];
        for (int i = 0; i < mResetList.size(); i++) {
            MyPoint point = mResetList.get(i);
            PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat(point.getPointName() + ".y", point.y, point.oy);
            holders[i] = holderY;

            PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat(point.getPointName() + ".x", point.x, point.ox);
            //假如size = 8,单存y方向的参数可存8个，最后一个索引为7，从索引8+i = 8,9,10...开始存x方向参数
            holders[mResetList.size() + i] = holderX;

        }
        return holders;
    }

    //重置回弹动画
    private void reset(PropertyValuesHolder[] holders) {
        final ValueAnimator animator;// 动画器
        animator = ValueAnimator.ofPropertyValuesHolder(holders);
        // 动画更新的监听
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {

                for (int i = 0; i < mResetList.size(); i++) {
                    MyPoint point = mResetList.get(i);
                    float valueY = (Float) arg0.getAnimatedValue(point.getPointName() + ".y");
                    float valueX = (Float) arg0.getAnimatedValue(point.getPointName() + ".x");
                    if (!isAbortAnim) {
                        //执行动画的核心方法
                        point.x = (int) valueX;
                        if (i > 2) {//前3个参数为body参数，不需要x方向移动
                            invalidate();
                        }
                        //动画执行完成
                        isResetFinish = point.x == point.ox;
                        Log.d(TAG," onAnimationUpdate");
                        isPointerMove = false;
                    }
                    // 根据最新value,更新布局
                    if (!isAbortAnim) {
                        point.y = (int) valueY;
                        invalidate();
                        //动画执行完成
                        isResetFinish = point.y == point.oy;
                        isPointerMove = false;
                    }
                }
            }
        });
        animator.setDuration(500);// 动画时间
        animator.setInterpolator(new OvershootInterpolator());
        animator.start();// 开启动画
    }

}

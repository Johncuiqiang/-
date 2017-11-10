package com.jibo.animationdemo.bean;

import com.jibo.animationdemo.common.MyPoint;

/**
 * Created by cuiqiang on 2016/11/9.
 * @author cuiqiang
 */
public class PointsBean {

    //身体相关参数
    private int mLeft;
    private int mTop;
    private int mRight;
    private int mBottom;
    private int midHeight;
    private int midLeft;
    private int midRight;

    private float mBodyWidth;
    private float mBodyHeight;
    //头部相关参数
    private float mCircleY;
    private float mCircleX;
    private float mCircleR;//头部圆心半径
    //眼睛相关参数
    private float mEyeLeftX;
    private float mEyeRightX;
    private float mEyeY;
    //鼻子相关参数
    private float mNoseY;
    private float mNoseX;
    private float mScreenWidth;
    private float mScreenHeight;

    //point对象
    //瞳孔
    private MyPoint mLeftEyeBallPoint;
    private MyPoint mRightEyeBallPoint;
    //身体
    private MyPoint mStartPointLeft;
    private MyPoint mEndPointLeft;
    private MyPoint mStartPointRight;
    private MyPoint mEndPointRight;
    private MyPoint mAssistPointLeft;
    private MyPoint mAssistPointRight;
    //头部
    private MyPoint mCirclePoint;
    private MyPoint mLeftEyePoint;
    private MyPoint mRightEyePoint;
    private MyPoint mNosePoint;

    public PointsBean(float screenWidth, float screenHeight) {
        this.mScreenHeight = screenHeight;
        this.mScreenWidth = screenWidth;
        initPoint();
        initOtherParams();//初始化Edge和ratio
    }

    private void initPoint() {

        mBodyWidth = mScreenWidth / 3;
        mBodyHeight = mScreenHeight / 4;
        mLeft = (int) (mScreenWidth / 3);
        mRight = (int) (mLeft + mBodyWidth);
        mTop = (int) (3 * mScreenHeight / 8);
        mBottom = (int) (mTop + mBodyHeight);
        midHeight = (mTop + mBottom) / 2;
        midLeft = (int) (mLeft - mScreenWidth / 6);
        midRight = (int) (mRight + mScreenWidth / 6);

        mCircleR = mBodyWidth / 2 + mScreenWidth / 10;//头部半径
        mCircleX = (midRight + midLeft) / 2f;
        mCircleY = mTop - (float) (Math.sqrt(mCircleR * mCircleR - Math.pow(mRight - mCircleX, 2)));
        mCirclePoint = new MyPoint((int) mCircleX, (int) mCircleY, "mCirclePoint");

        mStartPointLeft = new MyPoint(mLeft, mTop, "mStartPointLeft");
        mEndPointLeft = new MyPoint(mLeft, mBottom);
        mAssistPointLeft = new MyPoint(midLeft, midHeight, "mAssistPointLeft");
        mStartPointRight = new MyPoint(mRight, mTop);
        mEndPointRight = new MyPoint(mRight, mBottom);
        mAssistPointRight = new MyPoint(midRight, midHeight);

        mEyeLeftX = (mCirclePoint.x + mCircleR) / 2;
        mEyeRightX = mEyeLeftX + mCircleR - mCircleR / 6;
        mEyeY = (mCirclePoint.y + mCircleR) / 2;

        mLeftEyePoint = new MyPoint((int) mEyeLeftX, (int) mEyeY, "mLeftEyePoint");
        mRightEyePoint = new MyPoint((int) mEyeRightX, (int) mEyeY, "mRightEyePoint");
        mLeftEyeBallPoint = new MyPoint((int) mEyeLeftX, (int) mEyeY, "mLeftEyeBallPoint");
        mRightEyeBallPoint = new MyPoint((int) mEyeRightX, (int) mEyeY, "mRightEyeBallPoint");

        mNoseX = mCirclePoint.x;
        mNoseY = mCirclePoint.y + mCircleR / 8;
        mNosePoint = new MyPoint((int) mNoseX, (int) mNoseY, "mNosePoint");
    }

    //手指移动区间系数,（原理类似于：手指移动5，图像移动1）；
    private float mBodyRatio;
    private float mEyeRatio;
    private int leftScrollEdge;//眼睛左滑限制
    private int rightScrollEdge;//眼睛右滑限制
    private int upScrollEye;//眼眶上滑限制
    private int downScrollEye;//眼眶下滑限制
    private int upScrollEdge;//身体上滑限制
    private int downScrollEdge;//身体下滑限制

    private void initOtherParams() {
        upScrollEdge = (int) (mBodyHeight / 4f);
        downScrollEdge = (int) (mBodyHeight / 2f);
        upScrollEye = (int) (upScrollEdge + mCircleR / 6f);
        downScrollEye = (int) (downScrollEdge + mCircleR / 5f);
        leftScrollEdge = (int) (mCircleR / 3f);
        rightScrollEdge = (int) (mCircleR / 3f);
        mBodyRatio = mScreenHeight / mBodyHeight + 1;
        mEyeRatio = mBodyRatio * downScrollEdge / downScrollEye;//手指滑动总距离是一样的
    }

    public MyPoint getNosePoint() {
        return mNosePoint;
    }

    public MyPoint getRightEyePoint() {
        return mRightEyePoint;
    }

    public MyPoint getLeftEyePoint() {
        return mLeftEyePoint;
    }

    public MyPoint getLeftEyeBallPoint() {
        return mLeftEyeBallPoint;
    }

    public MyPoint getRightEyeBallPoint() {
        return mRightEyeBallPoint;
    }

    public MyPoint getEndPointLeft() {
        return mEndPointLeft;
    }

    public MyPoint getEndPointRight() {
        return mEndPointRight;
    }

    public MyPoint getStartPointRight() {
        return mStartPointRight;
    }

    public MyPoint getAssistPointLeft() {
        return mAssistPointLeft;
    }

    public MyPoint getAssistPointRight() {
        return mAssistPointRight;
    }

    public MyPoint getCirclePoint() {
        return mCirclePoint;
    }

    public MyPoint getStartPointLeft() {
        return mStartPointLeft;
    }

    public float getCircleR() {
        return mCircleR;
    }

    public float getBodyRatio() {
        return mBodyRatio;
    }

    public float getEyeRatio() {
        return mEyeRatio;
    }

    public int getLeftScrollEdge() {
        return leftScrollEdge;
    }

    public int getRightScrollEdge() {
        return rightScrollEdge;
    }

    public int getUpScrollEye() {
        return upScrollEye;
    }

    public int getDownScrollEye() {
        return downScrollEye;
    }

    public int getUpScrollEdge() {
        return upScrollEdge;
    }

    public int getDownScrollEdge() {
        return downScrollEdge;
    }
}

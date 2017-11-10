package com.jibo.animationdemo;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jibo.animationdemo.ui.MovePicAct;

/**
 * Created by cuiqiang on 2016/11/6.
 * @author cuiqiang
 */
public class MainActivity extends AppCompatActivity {

    private static final int SENSOR_SHAKE = 10;
    private RelativeLayout rlMain;
    private Button mBtnTurn;
    private SensorManager mSensorManager;
    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initView();
        initData();
    }

    private void initView() {
        rlMain = (RelativeLayout)findViewById(R.id.activity_main);
        mBtnTurn = (Button)findViewById(R.id.bt_turn);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

    }

    private void initData() {
        mBtnTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MovePicAct.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSensorManager != null) {// 注册监听器
            mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mSensorManager != null) {// 取消监听器
            mSensorManager.unregisterListener(sensorEventListener);
        }
    }
    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = 19;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
                mVibrator.vibrate(200);
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                mHandler.sendMessage(msg);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    /**
     * 动作执行
     */
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE:
                    animatorBg();
                    break;
                default:
                    break;
            }
        }

    };

    private void animatorBg() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlMain.setBackgroundColor(Color.BLUE);
            }
        }, 100);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlMain.setBackgroundColor(Color.YELLOW);
            }
        }, 200);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlMain.setBackgroundColor(Color.GREEN);
            }
        }, 300);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlMain.setBackgroundColor(Color.BLUE);
            }
        }, 400);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlMain.setBackgroundColor(Color.YELLOW);
            }
        }, 500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlMain.setBackgroundColor(Color.GREEN);
            }
        }, 600);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlMain.setBackgroundColor(Color.YELLOW);
            }
        }, 700);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlMain.setBackgroundColor(Color.GREEN);
            }
        }, 800);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlMain.setBackgroundColor(Color.BLUE);
            }
        }, 900);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlMain.setBackgroundColor(Color.YELLOW);
            }
        }, 1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlMain.setBackgroundColor(Color.GREEN);
            }
        }, 1100);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlMain.setBackgroundColor(Color.WHITE);
            }
        }, 1200);
    }
}

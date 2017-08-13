package com.example.toolbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toolbox.util.ElectricityBR;
import com.example.toolbox.view.CircleMenuLayout;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    //自定义圆盘菜单
    private CircleMenuLayout mCircleMenuLayout;
    //圆盘菜单显示文字
    private String[] mItemTexts = new String[]{"放大镜","工具尺","分贝测试仪",
            "手电筒","计算器","SOS"};
    private int[] mItemImgs = new int[]{R.drawable.home_mbank_1_normal,
            R.drawable.home_mbank_2_normal,R.drawable.home_mbank_3_normal,
            R.drawable.home_mbank_4_normal,R.drawable.home_mbank_5_normal,
            R.drawable.home_mbank_6_normal};
    //定义显示指南针图片控件
    private ImageView image_znz;
    //记录指南针转过的角度
    private float angle = 0f;
    //传感器管理器
    private SensorManager mSemsorManager;
    //剩余电量显示
    private TextView batterytv;
    //不同电量显示图片数组
    private int[] batteryStatusImags = new int[]{R.drawable.battery1,
            R.drawable.battery2,R.drawable.battery3};
    //电量多少切换图片的标准数组
    private int[] batteryStatusPercent = {75,30,0};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //隐藏ActionBar
        getSupportActionBar().hide();
        //指南针
        image_znz = (ImageView) findViewById(R.id.iv_znz);
        mSemsorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //为系统的方向传感器注册监听器
        mSemsorManager.registerListener(this,mSemsorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
        //电池信息显示
        batterytv = (TextView) findViewById(R.id.batterytv);


        //电池电量监听显示注册广播接收器
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatInfoReceiver,filter);


        //初始化圆盘控件
        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        //初始化圆盘控件菜单
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs,mItemTexts);
        mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                if(pos == 0){
                    Toast.makeText(getApplicationContext(),"单机菜单0按钮",Toast.LENGTH_SHORT).show();
                }
                if(pos == 1){
                    Toast.makeText(getApplicationContext(),"单机菜单1按钮",Toast.LENGTH_SHORT).show();
                }
                if(pos == 2){
                    Toast.makeText(getApplicationContext(),"单机菜单2按钮",Toast.LENGTH_SHORT).show();
                }
                if(pos == 3){
                    Toast.makeText(getApplicationContext(),"单机菜单3按钮",Toast.LENGTH_SHORT).show();
                }
                if(pos == 4){
                    Toast.makeText(getApplicationContext(),"单机菜单4按钮",Toast.LENGTH_SHORT).show();
                }
                if(pos == 5){
                    Toast.makeText(getApplicationContext(),"单机菜单5按钮",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void itemCenterClick(View view) {
                Toast.makeText(getApplicationContext(),"单机中心按钮",Toast.LENGTH_SHORT).show();

            }
        });
    }
    /**
     * 创建广播接收电池电量信息
     */
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                //当前电量
                int level = intent.getIntExtra("level", 0);
                //总电量
                int scale = intent.getIntExtra("scale", 100);
                onBatteryInfoReceiver(level,scale);
            }
        }
    };

    /**
     * 传感器报告新的值
     * @param sensorEvent
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //如果触发的传感器类型为水平传感器类型
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ORIENTATION){
            //获取绕Z轴转过的角度
            float Zangle = sensorEvent.values[0];
            //创建旋转动画 反转Zangle
            RotateAnimation ra = new RotateAnimation(angle,-Zangle,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0.5f
                    );
            //设置动画的持续时间
            ra.setDuration(200);
            //设置动画结束后的保留状态
            ra.setFillAfter(true);
            //启动动画
            image_znz.startAnimation(ra);
            angle = -Zangle;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * 生命周期结束时停止运行
     */
    @Override
    protected void onPause() {
        super.onPause();
        mSemsorManager.unregisterListener(this);
    }
    /**
     * 电池信息
     */
    public void onDC(View v){
        //接收电池电量信息
        ElectricityBR receiver = new ElectricityBR();
        //注册广播
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        MainActivity.this.registerReceiver(receiver,filter);
    }




    /**
     * 设置电量百分比，切换电池不同电量图片
     * @param intLevel
     * @param intScale
     */
    public void onBatteryInfoReceiver(int intLevel, int intScale) {
        int bp = intLevel*100/intScale;
        batterytv.setText(bp+"%");

        //电量大于等于0时设置电池背景为红色
        if(bp>=batteryStatusPercent[2]){
            batterytv.setBackgroundResource(batteryStatusImags[2]);
        }

        //电池电量大于30%时设置黄色背景
        if(bp>=batteryStatusPercent[1]){
            batterytv.setBackgroundResource(batteryStatusImags[1]);
        }
        //电池电量大于70%时设置绿色背景
        if(bp>=batteryStatusPercent[0]){
            batterytv.setBackgroundResource(batteryStatusImags[0]);
        }


    }

    /**
     * 生命周期重新开始时运行
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        mSemsorManager.registerListener(this,mSemsorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }


}

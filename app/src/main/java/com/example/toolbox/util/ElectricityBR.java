package com.example.toolbox.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Created by Administrator on 2017/8/13.
 */

public class ElectricityBR extends BroadcastReceiver {
    Dialog dialog = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
            //当前电量
            int level = intent.getIntExtra("level",0);
            //总电量
            int scale = intent.getIntExtra("scale",100);
            //电压
            int voltage = intent.getIntExtra("voltage",0);
            //当前电池温度
            int temperature = intent.getIntExtra("temperature",0);
            //电池类型
            String technology = intent.getStringExtra("technology");
            if(dialog == null){
                dialog = new AlertDialog.Builder(context)
                        .setTitle("电池电量")
                        .setMessage(
                                "电池电量为："+ String.valueOf(level*100/scale)
                                +"%\n"+"电池电压为："
                                +String.valueOf((float) voltage/1000)+"V"
                                +"\n电池类型为："+technology+"\n"+"电池温度为："
                                +String.valueOf((float)temperature/10)+"℃"
                        )
                        .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();
                dialog.show();

            }
        }
    }
}

package com.uniks.myfit;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.widget.TextView;

import java.lang.reflect.Array;

public class Accelerometer implements SensorEventListener{
    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    Sensor accelerometer;
    MainActivity mainActivity;
    //TextView xValue, yValue, zValue;

    public Accelerometer(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void init() {
        //xValue= (TextView) findViewById(R.id.xValue);
        //yValue=(TextView) findViewById(R.id.yValue);
        //zValue=(TextView)findViewById(R.id.zValue);
        Log.d(TAG, "onCreate: Intializing Sensor Services");
        sensorManager = (SensorManager) mainActivity.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "onCreate: Registered Accelerometer Listener");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        Log.d(TAG, "onSensorChanged: X:"+sensorEvent.values[0]+"Y:"+sensorEvent.values[1]+"Z:"+sensorEvent.values[2]);
        mainActivity.list.add(sensorEvent.values[0]);
        mainActivity.list.add(sensorEvent.values[1]);
        mainActivity.list.add(sensorEvent.values[2]);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

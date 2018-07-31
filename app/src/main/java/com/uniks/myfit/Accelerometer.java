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
        //Stroing The data
        float accelerationX = sensorEvent.values[0];
        float accelerationY = sensorEvent.values[1];
        float accelerationZ = sensorEvent.values[2];
        //Filtering the data
        if (Math.abs(accelerationX) < 1)
        { accelerationX = 0; }
        if (Math.abs(accelerationY) < 1)
        { accelerationY = 0; }
        if (Math.abs(accelerationZ) < 1)
        { accelerationZ = 0; }
        //printing the data
        Log.d(TAG, "onSensorChanged: X:"+accelerationX+"Y:"+accelerationY+"Z:"+accelerationZ);

        // store it into a list
        mainActivity.list.add(sensorEvent.values[0]);
        mainActivity.list.add(sensorEvent.values[1]);
        mainActivity.list.add(sensorEvent.values[2]);



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

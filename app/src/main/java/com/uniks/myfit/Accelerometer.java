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
    float accelerationX,accelerationY,accelerationZ;
    //TextView xValue, yValue, zValue;

    public Accelerometer(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void init() {

        sensorManager = (SensorManager) mainActivity.getSystemService(Context.SENSOR_SERVICE);
        Log.d(TAG, "onCreate: Intializing Accelerometer Services");
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "onCreate: Registered Accelerometer Listener");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        accelerationX= sensorEvent.values[0];
        accelerationY = sensorEvent.values[1];
        accelerationZ  = sensorEvent.values[2];

        displayAccValues();

        // store it into a list
        mainActivity.list.add(accelerationX);
        mainActivity.list.add(accelerationY);
        mainActivity.list.add(accelerationZ);

    }
    public void displayAccValues()
    {

        //Filtering the data
        if (Math.abs(accelerationX) < 1)
        { accelerationX = 0; }
        if (Math.abs(accelerationY) < 1)
        { accelerationY = 0; }
        if (Math.abs(accelerationZ) < 1)
        { accelerationZ = 0; }
        //display The data
        Log.d(TAG, "onSensorChanged: X:"+accelerationX+"Y:"+accelerationY+"Z:"+accelerationZ);
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

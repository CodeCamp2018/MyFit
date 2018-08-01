package com.uniks.myfit;

import android.app.Activity;
import android.os.Bundle;
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
    public float[] gravity;

    MainActivity mainActivity;
    float accelerationX,accelerationY,accelerationZ;
    //TextView xValue, yValue, zValue;

    public Accelerometer(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        gravity = new float[4];
    }

    public void init() {
        // Get an instance of the SensorManager
        sensorManager = (SensorManager) mainActivity.getSystemService(Context.SENSOR_SERVICE);
        Log.d(TAG, "onCreate: Intializing Accelerometer Services");

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "onCreate: Registered Accelerometer Listener");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        final  float alpha = (float) 0.8;
        /*Store Accelerometer x y z values */
        accelerationX= sensorEvent.values[0];
        accelerationY = sensorEvent.values[1];
        accelerationZ  = sensorEvent.values[2];



        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * sensorEvent.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * sensorEvent.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * sensorEvent.values[2];
        // Remove the gravity contribution with the high-pass filter.
        accelerationX =  sensorEvent.values[0] -  gravity[0];
        accelerationY =  sensorEvent.values[1] -  gravity[1];
        accelerationZ =  sensorEvent.values[2] -  gravity[2];
        // store it into a list
        mainActivity.list.add(accelerationX);
        mainActivity.list.add(accelerationY);
        mainActivity.list.add(accelerationZ);
        displayAccValues();
    }
    public void displayAccValues()
    {
        //display The data
        Log.i(TAG, "onSensorChanged: X:"+accelerationX+"Y:"+accelerationY+"Z:"+accelerationZ);
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

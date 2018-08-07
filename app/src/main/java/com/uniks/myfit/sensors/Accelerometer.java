package com.uniks.myfit.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import com.uniks.myfit.TrackingViewActivity;
import com.uniks.myfit.model.AccTripleVec;

import java.util.ArrayList;

public class Accelerometer implements SensorEventListener {
    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    Sensor accelerometer;
    public float[] gravity;
    public Context context;
    private static boolean running = true;

    TrackingViewActivity trackingViewActivity;
    float accelerationX, accelerationY, accelerationZ;
    float[] lowPass;

    AccTripleVec accTripleVec;
    AccTripleVec prevTriple;
    boolean rising;
    private final float treshold = 0.5f;
    private boolean skip = false;
    private int situpCount;

    //TextView xValue, yValue, zValue;

    public Accelerometer(TrackingViewActivity trackingViewActivity) {
        this.trackingViewActivity = trackingViewActivity;
        gravity = new float[3];
        lowPass = new float[3];

    }

    public void init() {
        rising = false;
        situpCount = 0;


        running = true;
        // Get an instance of the SensorManager
        sensorManager = (SensorManager) trackingViewActivity.getSystemService(Context.SENSOR_SERVICE);
        Log.d(TAG, "onCreate: Initializing Accelerometer Services");

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "onCreate: Registered Accelerometer Listener");
    }
    private static SensorEventListener sensorEventListener =
            new SensorEventListener() {
                public void onAccuracyChanged(Sensor sensor, int accuracy) {}
                public void onSensorChanged(SensorEvent event){}
            };

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (running) {
            final float beta = (float) 0.8;

            // Smooth out readings with a low-pass filter.
            accelerationX = beta * accelerationX + (1 - beta) * sensorEvent.values[0];
            accelerationY = beta * accelerationY + (1 - beta) * sensorEvent.values[1];
            accelerationZ = beta * accelerationZ + (1 - beta) * sensorEvent.values[2];

            lowPass[0] = beta * lowPass[0] + (1 - beta) * accelerationX;

            accTripleVec = new AccTripleVec(accelerationX, accelerationY,accelerationZ);
            displayAccValues();

            if (prevTriple == null) {
                prevTriple = accTripleVec;
                return;
            }

            if (accTripleVec.getSquaredMagnitude() > (prevTriple.getSquaredMagnitude() + treshold) && !rising) {
                rising = true;
            } else if (accTripleVec.getSquaredMagnitude() < (prevTriple.getSquaredMagnitude() - treshold) && rising) {
                rising = false;
                if (!skip) {
                    situpCount++;
                }
                skip = !skip;
            }

            prevTriple = accTripleVec;
        }
    }

    private void displayAccValues() {
        //display The data
        Log.i(TAG, "onSensorChanged: X:" + accelerationX + "Y:" + accelerationY + "Z:" + accelerationZ);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    //Unregisters listeners close accelerometer
    public void stopListening() {
        running = false;
        sensorManager.unregisterListener(sensorEventListener, accelerometer);
    }

    public int getSitupCount() {
        if (situpCount >= 1) {
            return situpCount - 1;
        }
        return  situpCount;
    }
}


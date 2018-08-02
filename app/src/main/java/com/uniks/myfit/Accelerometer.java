package com.uniks.myfit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import com.uniks.myfit.model.AccTripleVec;

public class Accelerometer implements SensorEventListener {
    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    Sensor accelerometer;
    public float[] gravity;
    public static Context context;
    private static boolean running = true;

    TrackingViewActivity trackingViewActivity;
    float accelerationX, accelerationY, accelerationZ;
    public float TotACC;
    private final float[] accelerometerReading = new float[3];

    //TextView xValue, yValue, zValue;

    public Accelerometer(TrackingViewActivity trackingViewActivity) {
        this.trackingViewActivity = trackingViewActivity;
        gravity = new float[4];
    }

    public void init() {
        running = true;
        // Get an instance of the SensorManager
        sensorManager = (SensorManager) trackingViewActivity.getSystemService(context.SENSOR_SERVICE);
        Log.d(TAG, "onCreate: Initializing Accelerometer Services");

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "onCreate: Registered Accelerometer Listener");
    }
    private static SensorEventListener sensorEventListener =
            new SensorEventListener() {
                public void onAccuracyChanged(Sensor sensor, int accuracy) {}
                public void onSensorChanged(SensorEvent event)
                {

                } };
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    if (running) {
                        final float alpha = (float) 0.8;
                        /*Store Accelerometer x y z values */
                        accelerationX = sensorEvent.values[0];
                        accelerationY = sensorEvent.values[1];
                        accelerationZ = sensorEvent.values[2];
                        // Isolate the force of gravity with the low-pass filter.
                        gravity[0] = alpha * gravity[0] + (1 - alpha) * sensorEvent.values[0];
                        gravity[1] = alpha * gravity[1] + (1 - alpha) * sensorEvent.values[1];
                        gravity[2] = alpha * gravity[2] + (1 - alpha) * sensorEvent.values[2];
                        // Remove the gravity contribution with the high-pass filter.
                        accelerationX = sensorEvent.values[0] - gravity[0];
                        accelerationY = sensorEvent.values[1] - gravity[1];
                        accelerationZ = sensorEvent.values[2] - gravity[2];
                        // store it into a list to send it to controller
                        trackingViewActivity.getAccelerometerQueue().add(new AccTripleVec(accelerationX, accelerationY,accelerationZ));
                        displayAccValues();

                        TotACC = (float) Math.sqrt(accelerationX * accelerationX + accelerationY * accelerationY + accelerationZ * accelerationZ);
                        if(TotACC>0.5) {
                            Log.d("Device motion detected!", System.currentTimeMillis() + "," + TotACC);
                        }
                    }
                }

                public void displayAccValues() {
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
        Toast.makeText (context, "Sensor Stopped..", Toast.LENGTH_SHORT).show();
    }
}


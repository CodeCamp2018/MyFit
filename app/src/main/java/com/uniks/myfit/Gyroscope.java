package com.uniks.myfit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import static android.util.Half.EPSILON;
import static java.lang.Math.sqrt;
import android.app.Activity;
import android.os.Bundle;
public class Gyroscope implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor gyro;

    // Create a constant to convert nanoseconds to seconds.
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    float[] deltaRotationMatrix = new float[9];
    public float[] axis;
    private float timestamp;
    private MainActivity mainActivity;

    public Gyroscope(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        axis = new float[4];
    }

    public void init() {
        sensorManager = (SensorManager) mainActivity.getSystemService(Context.SENSOR_SERVICE);
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public void onSensorChanged(SensorEvent event) {

        if (timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NS2S;
            // Axis of the rotation sample, not normalized yet.
            float gravityX = event.values[0];
            float gravityY = event.values[1];
            float gravityZ = event.values[2];
            // Calculate the angular speed of the sample
            float norm = (float) Math.sqrt(gravityX * gravityX + gravityY * gravityY + gravityZ * gravityZ);

            // Normalize the rotation vector if it's big enough to get the axis
            if (norm > EPSILON) {
                gravityX/=norm;
                gravityY/=norm;
                gravityZ/=norm;
            }
            float thetaOverTwo = norm * dT / 2.0f;
            float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
            float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
            axis[0]=sinThetaOverTwo *gravityX;
            axis[1]=sinThetaOverTwo *gravityY;
            axis[2]=sinThetaOverTwo *gravityZ;
            axis[3]=cosThetaOverTwo;

        }

             timestamp = event.timestamp;
             SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);


    }
}

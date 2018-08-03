package com.uniks.myfit.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.uniks.myfit.TrackingViewActivity;

public class ProximitySensorService implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor proximity;
    private static final int SENSOR_SENSITIVITY = 4;
    private static boolean isSensorPresent = true;
    private static boolean Inrange;
    public static Context context;
    TrackingViewActivity trackingViewActivity;
    private float distanceFromPhone;
    int pushupcount;

    public ProximitySensorService(TrackingViewActivity trackingViewActivity) {
        this.trackingViewActivity = trackingViewActivity;
    }

    public void initialize() {
        sensorManager = (SensorManager) trackingViewActivity.getSystemService(context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
            proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            isSensorPresent = true;
        } else {
            isSensorPresent = false;
        }
        sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
        /*Do Nothing*/

    }

    public int getCalculatedPushUps() {

        return pushupcount;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isSensorPresent) {
            distanceFromPhone = event.values[0];
            if (distanceFromPhone <= proximity.getMaximumRange()) {
                Inrange = true;
                if (distanceFromPhone == 0) {
                    pushupcount++;

                }
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void stopListening() {
        sensorManager.unregisterListener(this, proximity);
    }

}

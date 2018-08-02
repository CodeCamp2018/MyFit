package com.uniks.myfit.model;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.uniks.myfit.MainActivity;
import com.uniks.myfit.R;
import com.uniks.myfit.TrackingViewActivity;

public class ProximitySensorService  implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor proximity;
    private static final int SENSOR_SENSITIVITY = 4;
    private static boolean isSensorPresent = true;
    private static boolean Inrange;
    public static Context context;
    TrackingViewActivity trackingViewActivity;
    private float distanceFromPhone;

    public ProximitySensorService(TrackingViewActivity trackingViewActivity)
    {
        this.trackingViewActivity=trackingViewActivity;
    }
    public void initialize()
    {
        sensorManager = (SensorManager) trackingViewActivity.getSystemService(context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)!=null)
        {
            proximity=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            isSensorPresent=true;
        }
        else
        {
            isSensorPresent=false;
        }
        sensorManager.registerListener(this, proximity,SensorManager.SENSOR_DELAY_NORMAL);
             /*Do Nothing*/

}


    public void onResume() {
        if (isSensorPresent) {
            sensorManager.registerListener(this, proximity,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }


    public void onPause() {
        if (isSensorPresent) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isSensorPresent) {
            distanceFromPhone = event.values[0];
            if (distanceFromPhone < proximity.getMaximumRange()) {
                Inrange=true;
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private static SensorEventListener sensorEventListener =
            new SensorEventListener() {
                public void onAccuracyChanged(Sensor sensor, int accuracy) {}
                public void onSensorChanged(SensorEvent event)
                {

                } };
    public void stopListening() {
        sensorManager.unregisterListener(sensorEventListener,proximity );
        Toast.makeText(context, "Sensor Stopped..", Toast.LENGTH_SHORT).show();
    }

}

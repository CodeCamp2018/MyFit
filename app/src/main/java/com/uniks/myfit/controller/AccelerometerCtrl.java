package com.uniks.myfit.controller;
import com.uniks.myfit.Accelerometer;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.List;

public class AccelerometerCtrl implements SensorEventListener {

    public AccelerometerCtrl(Accelerometer accelerometerSensor) {
    }


    public void onCreate() {

    }

    public void onSensorChanged(SensorEvent event) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
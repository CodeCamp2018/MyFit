package com.uniks.myfit.controller;
import com.uniks.myfit.Accelerometer;
import com.uniks.myfit.Gyroscope;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.List;

public class SitUpsCtrl  {
    Gyroscope gyro;
    Accelerometer accelerometerSensor;
    final  float alpha = (float) 0.8;

    public SitUpsCtrl(Accelerometer accelerometerSensor, Gyroscope gyro) {
        this.gyro = gyro;
        this.accelerometerSensor=accelerometerSensor;
    }



}
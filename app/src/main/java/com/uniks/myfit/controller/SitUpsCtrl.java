package com.uniks.myfit.controller;
import com.uniks.myfit.Accelerometer;
import com.uniks.myfit.Gyroscope;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.List;

public class SitUpsCtrl  {

    Accelerometer accelerometerSensor;


    public SitUpsCtrl(Accelerometer accelerometerSensor) {
        this.accelerometerSensor=accelerometerSensor;

    }



}
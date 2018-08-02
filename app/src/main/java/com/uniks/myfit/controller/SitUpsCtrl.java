package com.uniks.myfit.controller;
import com.uniks.myfit.Accelerometer;
import com.uniks.myfit.Gyroscope;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.List;

public class SitUpsCtrl  {
    int idxCount = 0; // index counter
    double previousacc = 0; // start Timestamp
    double currentacc = 0; // current timestamp for iteration
    Accelerometer accelerometerSensor;


    public SitUpsCtrl(Accelerometer accelerometerSensor) {
        this.accelerometerSensor = accelerometerSensor;
        /*do {

        }while ( != 0.0);

    }*/


    }


}
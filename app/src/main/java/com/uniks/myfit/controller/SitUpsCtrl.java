package com.uniks.myfit.controller;
import com.uniks.myfit.Accelerometer;
import com.uniks.myfit.Gyroscope;
import com.uniks.myfit.TrackingViewActivity;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.List;

public class SitUpsCtrl {

    private int situpCount; // situp counter
    public static final String ACC_THRESHOLD_MIN_VALUE = "threshold_min_value";
    public static final String ACC_THRESHOLD_MAX_VALUE = "threshold_max_value";
    Accelerometer accelerometerSensor;
    private TrackingViewActivity trackingViewActivity;
    Accelerometer accSensor;
    private boolean active;
    private int actualState;

    public SitUpsCtrl(TrackingViewActivity trackingViewActivity) {
        this.trackingViewActivity = trackingViewActivity;
        accSensor = new Accelerometer(trackingViewActivity);
    }

    public void init() {
        situpCount = 0;
        actualState = 0;
        active = true;
        accSensor.init();
    }

    public void calculateSitups() {
        float prevValue = Float.MAX_VALUE;
        while (active) {
            switch (actualState) {
                case 0: // start
                    break;
                case 1: // found rising
                    // if the current value in Array is < the previous change state to 2
                    break;
                case 2: // found falling -> peak found
                    // if change is found then make counter +
                    // change state to 0
                    break;
            }
        }
    }

    public int getSitupCount() {
        return situpCount;
    }
}
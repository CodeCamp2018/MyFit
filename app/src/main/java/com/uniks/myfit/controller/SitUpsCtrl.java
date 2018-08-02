package com.uniks.myfit.controller;
import com.uniks.myfit.Accelerometer;
import com.uniks.myfit.Gyroscope;
import com.uniks.myfit.TrackingViewActivity;
import com.uniks.myfit.model.AccTriple;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Iterator;
import java.util.List;

public class SitUpsCtrl {

    private int situpCount; // situp counter
    public static final String  rising = "found";
    public static final String falling = "found";
    Accelerometer accelerometerSensor;
    private TrackingViewActivity trackingViewActivity;
    Accelerometer accSensor;
    AccTriple accTriple;
    private Boolean Flag;
    private boolean active;
    private int actualState;
    private int countIndex;

    public SitUpsCtrl(TrackingViewActivity trackingViewActivity) {
        this.trackingViewActivity = trackingViewActivity;
        accSensor = new Accelerometer(trackingViewActivity);
    }

    public void init() {
        situpCount = 0;
        actualState = 0;
        countIndex = 0;
        active = true;

        accSensor.init();
    }

    public int calculateSitups() {
        active = true;
        AccTriple prevTriple = new AccTriple();
        accTriple = trackingViewActivity.getAccelerometerQueue().get(countIndex);
        while (active) {
            switch (actualState) {
                case 0:
                    prevTriple = accTriple;
                    accTriple = trackingViewActivity.getAccelerometerQueue().get(countIndex++);

                    if (accTriple != null) {

                        // check for rising
                        if (accTriple.getX() > prevTriple.getX() && accTriple.getY() > prevTriple.getY()) {
                            actualState = 1;

                        }
                    } else {
                        active = false;
                    }
                    // if rising found

                    break;
                case 1: // if the current value in Array is < the previous change state to 2
                    prevTriple = accTriple;
                    accTriple = trackingViewActivity.getAccelerometerQueue().get(countIndex++);

                    if(accTriple != null) {
                        if (accTriple.getX() < prevTriple.getX() && (accTriple.getY() < prevTriple.getY() && accTriple.getZ() < prevTriple.getZ())) {
                            // found Falling
                            actualState = 2;
                        }
                    } else {
                        active = false;
                    }

                    break;
                case 2: // found falling -> peak found
                    // if change is found then make counter +
                    // change state to 0
                    situpCount++;
                    actualState = 0;
                    break;
            }
        }
        return situpCount;
    }

}
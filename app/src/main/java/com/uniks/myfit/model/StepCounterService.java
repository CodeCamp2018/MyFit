package com.uniks.myfit.model;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.uniks.myfit.MainActivity;
import com.uniks.myfit.controller.SitUpsCtrl;
import com.uniks.myfit.controller.StepsCtrl;

import java.util.ArrayList;
import java.util.List;

import java.util.List;

import static android.content.ContentValues.TAG;

public class StepCounterService  implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensorCount;
    MainActivity mainActivity;
    StepsCtrl stepsCtrl;

    public StepCounterService(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void onStart()//Command(Intent intent, int flags, int startId)
    {
        //If it's available we can retrieve the value using following code
        sensorManager = (SensorManager) mainActivity.getSystemService(Context.SENSOR_SERVICE);

        sensorCount= sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        stepsCtrl = new StepsCtrl();
        if(sensorCount!= null) {
            sensorManager.registerListener(this, sensorCount, SensorManager.SENSOR_DELAY_UI);
        }
        else
        {
            //Log.e(TAG, "No step counter present: ", );
        }

        //Check if the stepCounter is available first.
        List<Sensor> gravSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        for (Sensor each : gravSensors) {
            //check for sensor named step counter in the list.
            Log.d("Sesnor_list", each.getName());
        }

        //return START_STICKY;
    }


   // @Nullable
    //@Override
   // public IBinder onBind(Intent intent) {
      //  return null;
    //}



    //An onSensorChanged event gets triggered every time new step count is detected.
    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == sensorCount.TYPE_STEP_COUNTER)
        {
            //tolerance can be put here after testing walking
            Log.d("step_count = ", String.valueOf(sensorCount.getFifoMaxEventCount()));
            stepsCtrl.addStep();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
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

import java.util.ArrayList;
import java.util.List;

import java.util.List;

public class StepCounterService  implements SensorEventListener {
    private SensorManager sensorManager;
    Sensor sensor;
    MainActivity mainActivity;

    public StepCounterService(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void onStart()//Command(Intent intent, int flags, int startId)
    {
        //If it's available we can retrieve the value using following code
        sensorManager = (SensorManager) mainActivity.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        
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

        if(event.sensor.getType() == sensor.TYPE_STEP_COUNTER)
        {
            Log.d("step_count = ", String.valueOf(event.values));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
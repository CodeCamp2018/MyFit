package com.uniks.myfit.model;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import com.uniks.myfit.MainActivity;
import com.uniks.myfit.R;
import com.uniks.myfit.TrackingViewActivity;

import java.util.List;

public class StepCounterService implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensorCount;
    TrackingViewActivity trackingViewActivity;
    private float startStepCounter = 0;
    private float endStepCounter = 0;
    private boolean active;
    private int actualCount;

    public StepCounterService(TrackingViewActivity trackingViewActivity) {
        this.trackingViewActivity = trackingViewActivity;
    }

    public void onStart()//Command(Intent intent, int flags, int startId)
    {
        active = true;

          PackageManager pm = trackingViewActivity.getPackageManager();
          if (pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)) {
              //If it's available we can retrieve the value using following code
              sensorManager = (SensorManager) trackingViewActivity.getSystemService(Context.SENSOR_SERVICE);

              sensorCount = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

              if (sensorCount != null) {
                  sensorManager.registerListener(this, sensorCount, SensorManager.SENSOR_DELAY_UI);
              } else {
                  //Log.e(TAG, "No step counter present: ", );
              }

              //Check if the stepCounter is available first.
              List<Sensor> gravSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

              for (Sensor each : gravSensors) {
                  //check for sensor named step counter in the list.
              }


          } else {
              Log.e("StepCounterService", "does not have step_counter_sensor");
              // TODO: inform the user (?)
          }



        //return START_STICKY;
    }

    public void onStop() {
        sensorManager.unregisterListener(this, sensorCount);
        active = false;
    }


    // @Nullable
    //@Override
    // public IBinder onBind(Intent intent) {
    //  return null;
    //}


    //An onSensorChanged event gets triggered every time new step count is detected.
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == sensorCount.TYPE_STEP_COUNTER) {
            //tolerance can be put here after testing walking
            if (active) {
                if (startStepCounter == 0) {
                    startStepCounter = event.values[0];
                }

                endStepCounter = event.values[0];

                actualCount = (int) (endStepCounter - startStepCounter);
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public int getActualCount() {
        return actualCount;
    }
}
package com.uniks.myfit.model;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import com.uniks.myfit.MainActivity;
import com.uniks.myfit.R;
import com.uniks.myfit.TrackingViewActivity;
import com.uniks.myfit.controller.StepsCtrl;

import java.util.List;

public class StepCounterService implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensorCount;
    TrackingViewActivity trackingViewActivity;
    private int startStepCounter = 0;
    private int endStepCounter = 0;
    private boolean active;

    public StepCounterService(TrackingViewActivity trackingViewActivity) {
        this.trackingViewActivity = trackingViewActivity;
    }

    public void onStart()//Command(Intent intent, int flags, int startId)
    {
        active = true;
        TextView stepCounterTitleUI = trackingViewActivity.findViewById(R.id.title_2);
        stepCounterTitleUI.setText("Steps");

        //If it's available we can retrieve the value using following code
        sensorManager = (SensorManager) trackingViewActivity.getSystemService(Context.SENSOR_SERVICE);

        sensorCount = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        stepsCtrl = new StepsCtrl();
        if (sensorCount != null) {
            sensorManager.registerListener(this, sensorCount, SensorManager.SENSOR_DELAY_UI);
        } else {
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

    public void onStop() {
        active = false;
        // TODO: save to db
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
            Log.d("step_count = ", String.valueOf(sensorCount.getFifoMaxEventCount()));
            if (active) {
                if (startStepCounter == 0) {
                    startStepCounter = sensorCount.getFifoMaxEventCount();
                }
                endStepCounter = sensorCount.getFifoMaxEventCount();

                int actualCount = endStepCounter - startStepCounter;

                TextView stepCounterValueUI = trackingViewActivity.findViewById(R.id.value_2);

                stepCounterValueUI.setText(Integer.toString(actualCount));
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public StepsCtrl getStepsCtrl() {
        return stepsCtrl;
    }

    public void setStepsCtrl(StepsCtrl stepsCtrl) {
        this.stepsCtrl = stepsCtrl;
    }
}
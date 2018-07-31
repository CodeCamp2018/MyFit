package com.uniks.myfit.model;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.uniks.myfit.MainActivity;
import com.uniks.myfit.R;

public class ProximitySensorService  implements SensorEventListener {

    private SensorManager SensorManager;
    private Sensor Proximity;
    private static final int SENSOR_SENSITIVITY = 4;

    MainActivity mainActivity;

    public ProximitySensorService(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    public void onStart()
    {
        SensorManager = (SensorManager) mainActivity.getSystemService(Context.SENSOR_SERVICE);
        if (SensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
            Proximity = SensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
        else
            {
             /*Do Nothing*/
            }
        }


    public void onResume() {

        SensorManager.registerListener(this, Proximity, SensorManager.SENSOR_DELAY_NORMAL);
    }


    public void onPause() {

        SensorManager.unregisterListener(this);
    }
    //proximity sensor Listeners
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            Log.d("step_count = ", String.valueOf(event.values));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}

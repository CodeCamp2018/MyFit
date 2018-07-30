package com.uniks.myfit;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.uniks.myfit.database.AppDatabase;

public class MainActivity extends AppCompatActivity  implements SensorEventListener{
    private static final String TAG="MainActivity";
    private SensorManager sensorManager;
    AppDatabase db;

    private Sensor mGyro;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setup the database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "myFitDB").build();


        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: registered Accelerometer Lisener");






        Log.d(TAG, "onCreate: intiliazing sensor services");
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //Creating proximity Sensor Object
        Sensor proximitySensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        //proximity sensor Listeners
        SensorEventListener proximitySensorListener=new SensorEventListener()
        {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }


        }
        //registered Accelerometer Lisener
        Sensor accelerometer= sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        //registered Proximity Lisenner
        sensorManager.registerListener(proximitySensorListener,proximitySensor,2*1000*1000);
        Log.d(TAG, "onCreate: registered Accelerometer Lisener");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(TAG, "onSensorChanged: X:"+sensorEvent.values[0]+"Y:"+sensorEvent.values[1]+"z"+sensorEvent.values[2]);
    }
}
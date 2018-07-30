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
    Sensor accelerometer;
    AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setup the database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "myFitDB").build();


        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: intiliazing sensor services");
        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
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
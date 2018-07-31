package com.uniks.myfit;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.uniks.myfit.controller.AccelerometerCtrl;
import com.uniks.myfit.database.AppDatabase;
import com.uniks.myfit.database.SportExercise;
import com.uniks.myfit.database.User;
import com.uniks.myfit.model.StepCounterService;
import com.uniks.myfit.model.UserData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final String databaseName = "myFitDB.db";
    private static final String TAG = "BasicSensorsApi";
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];

    public ArrayList<Float> list = new ArrayList<>();
    Sensor proximity;
    Accelerometer accelerometerSensor;
    AccelerometerCtrl accelerometerCtrl;
    Gyroscope gyroscopeSensor;
    StepCounterService stepcounter;
    AppDatabase db;
    List<User> users;
    List<SportExercise> sportExercises;

    private float timestamp;
    private Sensor mGyro;
    //private Sensor accelerometer;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setup the database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, databaseName).build();

        // if there is no user, create one
        users = db.userDao().getAll();

        if (users.isEmpty()) {
            User newUser = new User();
            newUser.setWeight(65);
            db.userDao().insert(newUser);
            users = db.userDao().getAll();
        }
        Log.d(TAG, "onCreate: initializing sensor services");

        /* Accelerometer Sensor Class*/
        accelerometerSensor = new Accelerometer(this);
        /* Accelerometer Control Class*/
        accelerometerCtrl = new AccelerometerCtrl(accelerometerSensor);
        /* Initialize The Accelerometer Sensor*/
        accelerometerSensor.init();
        /* Gyroscope Sensor Class*/
        gyroscopeSensor = new Gyroscope(this);
        /* Gyroscope Init*/
        gyroscopeSensor.init();
        /* StepCounter Software Sensor*/
       stepcounter = new StepCounterService(this);
       stepcounter.onStart();



        setContentView(R.layout.activity_main);


        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
                //Creating proximity Sensor Object
                Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
            proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        } else
        {
            // Failure! No proximity.
        }

        //proximity sensor Listeners
        SensorEventListener proximitySensorListener=new SensorEventListener()
        {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}

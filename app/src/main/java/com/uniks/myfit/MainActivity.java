package com.uniks.myfit;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.uniks.myfit.controller.AccelerometerCtrl;
import com.uniks.myfit.database.AppDatabase;
import com.uniks.myfit.database.SportExercise;
import com.uniks.myfit.database.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final String databaseName = "myFitDB.db";
    private static final String TAG = "BasicSensorsApi";
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];

    public ArrayList<Float> list = new ArrayList<>();

    Sensor gyroscope;
    Sensor stepDetector;
    Sensor proximity;
    Accelerometer accelerometerSensor;
    Gyroscope gyroscopeSensor;
    AccelerometerCtrl accelerometerCtrl;
    AppDatabase db;
    User user;
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
        List<User> users = db.userDao().getAll();

        if (users.isEmpty()) {
            User newUser = new User();
            newUser.setWeight(65);
            db.userDao().insert(newUser);
            users = db.userDao().getAll();
        }

        user = users.get(0); // for this small project there is only one user

        accelerometerSensor = new Accelerometer(this);
        accelerometerCtrl = new AccelerometerCtrl(accelerometerSensor);
        accelerometerSensor.init();
        gyroscopeSensor = new Gyroscope(this);
        gyroscopeSensor.init();

        accelerometerCtrl = new AccelerometerCtrl(accelerometerSensor);


        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        Log.d(TAG, "onCreate: registered Accelerometer Lisener");


        Log.d(TAG, "onCreate: initializing sensor services");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        // Success! There's a Accelerometer


        //Creating proximity Sensor Object
        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        } else {// Failure! No gyroscope.
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        } else {// Failure! No Step counter.
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
            proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        } else {// Failure! No proximity.
        }
        //SensorManager.registerListener(MainActivity.this,Accelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        Log.d(TAG, "onCreate: registered Accelerometer Listener");

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //Creating proximity Sensor Object
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        //proximity sensor Listeners
        SensorEventListener proximitySensorListener = new SensorEventListener() {
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
        db.userDao().updateUser(user);
        for (SportExercise exercise : sportExercises) {
            db.sportExerciseDao().updateSportExercises(exercise);
        }
        db.close();
        super.onDestroy();
    }
}

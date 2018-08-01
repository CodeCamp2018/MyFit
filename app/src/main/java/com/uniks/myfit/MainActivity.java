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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.uniks.myfit.controller.AccelerometerCtrl;
import com.uniks.myfit.database.AppDatabase;
import com.uniks.myfit.database.SportExercise;
import com.uniks.myfit.database.User;
import com.uniks.myfit.model.ProximitySensorService;
import com.uniks.myfit.model.StepCounterService;
import com.uniks.myfit.helper.WeightTxtListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final String databaseName = "myFitDB";
    private static final String TAG = "BasicSensorsApi";
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];

    public ArrayList<Float> list = new ArrayList<>();

    Accelerometer accelerometerSensor;
    AccelerometerCtrl accelerometerCtrl;
    Gyroscope gyroscopeSensor;
    StepCounterService stepcounter;
    ProximitySensorService proximity;
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
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, databaseName).allowMainThreadQueries().build();

        // if there is no user, create one
        List<User> users = db.userDao().getAll();

        if (users == null || users.isEmpty()) {
            // empty table so create an dummy user
            User newUser = new User();
            newUser.setWeight(65);
            db.userDao().insert(newUser);
            users = db.userDao().getAll();
        }

        user = users.get(0); // for this small project there is only one user

        Log.d(TAG, "onCreate: initializing sensor services");
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
        /* Step Count Init*/
        stepcounter.onStart();
        proximity = new ProximitySensorService(this);
        proximity.onStart();

        // set layout
        setContentView(R.layout.activity_main);
        EditText weightTxt = findViewById(R.id.input_weight);
        weightTxt.setText(String.valueOf(user.getWeight()), TextView.BufferType.EDITABLE);
        weightTxt.addTextChangedListener(new WeightTxtListener(db, user));


        Log.d(TAG, "onCreate: registered Accelerometer Lisener");

       /* SensorEventListener proximitySensorListener=new SensorEventListener()
        {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };*/
    }

    @Override
    protected void onDestroy() {
        db.userDao().updateUser(user);
        for (SportExercise exercise : sportExercises) {
            db.sportExerciseDao().updateSportExercises(exercise);
        }
        db.close();
        // TODO: close the sensors!
        super.onDestroy();
    }
}

package com.uniks.myfit;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.uniks.myfit.controller.CardsRecyclerViewAdapter;
import com.uniks.myfit.controller.SitUpsCtrl;
import com.uniks.myfit.controller.SitUpsCtrl;
import com.uniks.myfit.database.AppDatabase;
import com.uniks.myfit.database.SportExercise;
import com.uniks.myfit.database.User;
import com.uniks.myfit.helper.StartButtonHelper;
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

    Gyroscope gyroscopeSensor;

    public AppDatabase db;
    User user;
    List<SportExercise> sportExercises;

    private RecyclerView cardRecyclerView;
    private RecyclerView.Adapter cardsAdapter;
    private RecyclerView.LayoutManager cardsLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // model
        // setup the database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, databaseName).allowMainThreadQueries().fallbackToDestructiveMigration().build();

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
        // controllers
        Log.d(TAG, "onCreate: initializing sensor services");
        /* Gyroscope Sensor Class*/
        gyroscopeSensor = new Gyroscope(this);
        /* Gyroscope Init*/
        gyroscopeSensor.init();
        // view
        setContentView(R.layout.activity_main);

        // set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText weightTxt = findViewById(R.id.input_weight);
        weightTxt.setText(String.valueOf(user.getWeight()), TextView.BufferType.EDITABLE);
        weightTxt.addTextChangedListener(new WeightTxtListener(db, user));

        /*
         * Listen to buttons to start tracking
         */
        FloatingActionButton startRunning = (FloatingActionButton) findViewById(R.id.add_exercise_running);
        setStartListener(0, startRunning);

        FloatingActionButton startCycling = (FloatingActionButton) findViewById(R.id.add_exercise_cycling);
        setStartListener(1, startCycling);

        FloatingActionButton startPushups = (FloatingActionButton) findViewById(R.id.add_exercise_pushups);
        setStartListener(2, startPushups);

        FloatingActionButton startSitups = (FloatingActionButton) findViewById(R.id.add_exercise_situps);
        setStartListener(3, startSitups);

        // set cards of completed tours
        cardRecyclerView = findViewById(R.id.cards_recycler_view);
        cardRecyclerView.setHasFixedSize(true);
        cardsLayoutManager = new LinearLayoutManager(this);
        cardRecyclerView.setLayoutManager(cardsLayoutManager);
        cardsAdapter = new CardsRecyclerViewAdapter(getDataSet());
        cardRecyclerView.setAdapter(cardsAdapter);

    }

    private ArrayList<SportExercise> getDataSet() {

        List<SportExercise> allUsers = db.sportExerciseDao().getAllFromUser(user.getUid());

        return new ArrayList<SportExercise>(allUsers);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((CardsRecyclerViewAdapter) cardsAdapter).setOnItemClickListener(
                new CardsRecyclerViewAdapter.MyClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Log.i("ClickEvent on Card:", " Clicked on Item " + position);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    private void setStartListener(int modeCode, FloatingActionButton startButton) {
        startButton.setOnClickListener(new StartButtonHelper(modeCode, this));
    }
}

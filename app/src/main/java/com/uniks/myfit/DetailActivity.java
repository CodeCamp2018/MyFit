package com.uniks.myfit;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.uniks.myfit.controller.DetailViewMapsController;
import com.uniks.myfit.database.AppDatabase;
import com.uniks.myfit.database.LocationData;
import com.uniks.myfit.database.SportExercise;
import com.uniks.myfit.database.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    AppDatabase db;
    User user;
    SportExercise exercise;
    List<LocationData> allLocation;


    DetailViewMapsController detailViewMapsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Model
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, MainActivity.databaseName).allowMainThreadQueries().fallbackToDestructiveMigration().build();

        user = db.userDao().getAll().get(0); // same call as in MainActivity -> for this project ok, because just one user

        int index = getIntent().getIntExtra("POSITION", 0);

        exercise = db.sportExerciseDao().getAllFromUser(user.getUid()).get(index); // get the clicked exercise of all user exercises

        allLocation = db.locationDataDao().getAllFromExercise(exercise.getId());

        // View
        switch (exercise.getMode()) {
            case 0:
                this.setTitle("Running");
                break;
            case 1:
                this.setTitle("Cycling");
                break;
            case 2:
                this.setTitle("Push Ups");
                break;
            case 3:
                this.setTitle("Sit Ups");
                break;
        }

        // also choose layout based on exercise type from stored data

        if (exercise.getMode() == 0 || exercise.getMode() == 1) {
            setContentView(R.layout.activity_detail_tracked);
        } else {
            setContentView(R.layout.activity_detail_repetitions);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.share_button);
        fab.setOnClickListener(this);

        // Controller
        detailViewMapsController = new DetailViewMapsController(this, exercise, db);

        if (exercise.getMode() == 0 || exercise.getMode() == 1) {
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(detailViewMapsController);
        }

        setExerciseData();
    }

    private void setExerciseData() {
        TextView startTime = findViewById(R.id.exercise_started_time);
        TextView duration = findViewById(R.id.exercise_duration);

        startTime.setText(formattedDate());
        duration.setText(exercise.getTripTime());

        switch (exercise.getMode()) {
            case 0:
                // Running

                TextView runningDistance = findViewById(R.id.exercise_distance);
                TextView steps = findViewById(R.id.exercise_steps_count);

                runningDistance.setText(String.format("The distance you run was %.2f km.", exercise.getDistance()));
                steps.setText(String.format("Congratulations you stepped %s steps during your exercise.", String.valueOf(exercise.getAmountOfRepeats())));

                break;
            case 1:
                // Cycling

                TextView cyclingDistance = findViewById(R.id.exercise_distance);

                cyclingDistance.setText(String.format("The distance you cycled was %.2f km.", exercise.getDistance()));

                break;
            case 2:
                // Push Ups

                TextView pushupsRepetitions = findViewById(R.id.exercise_repetitions);

                pushupsRepetitions.setText(String.format("Congratulations, you did %d push ups.", exercise.getAmountOfRepeats()));

                break;
            case 3:
                // Sit Ups

                TextView situpsRepetitions = findViewById(R.id.exercise_repetitions);

                situpsRepetitions.setText(String.format("Congratulations, you did %d sit ups.", exercise.getAmountOfRepeats()));

                break;
        }
    }

    private String formattedDate() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.GERMANY);


        return sdf.format(exercise.getDate());
    }

    @Override
    public void onClick(View v) {

        if (exercise.getMode() == 0 || exercise.getMode() == 1) {
            detailViewMapsController.doMapScreenshot();
        } else {

            String shareBody = null; // "Write your Body here";
            try {
                shareBody = getJsonFromExerciseData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String shareSub = "Exercise data from MyFit";
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

            startActivity(Intent.createChooser(shareIntent, "Share Exercise Data"));

        }

    }

    private String getJsonFromExerciseData() throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("start", exercise.getDate());
        obj.put("duration", exercise.getTripTime());

        switch (exercise.getMode()) {

            case 2: // pushups

                obj.put("pushUps", exercise.getAmountOfRepeats());

                break;
            case 3: // situps

                obj.put("sitUps", exercise.getAmountOfRepeats());

                break;

        }

        return obj.toString();
    }

}
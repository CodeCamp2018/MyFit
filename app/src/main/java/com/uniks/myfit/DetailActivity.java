package com.uniks.myfit;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.uniks.myfit.controller.MapsController;
import com.uniks.myfit.database.AppDatabase;
import com.uniks.myfit.database.SportExercise;
import com.uniks.myfit.database.User;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    AppDatabase db;
    User user;
    SportExercise exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Model
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, MainActivity.databaseName).allowMainThreadQueries().fallbackToDestructiveMigration().build();

        user = db.userDao().getAll().get(0); // same call as in MainActivity -> for this project ok, because just one user

        int index = getIntent().getIntExtra("EXERCISE", 0);

        exercise = db.sportExerciseDao().getAllFromUser(user.getUid()).get(index); // get the clicked exercise of all user exercises



        // View
        this.setTitle(exercise.getMode());

        // also choose layout based on exercise type from stored data
        if (exercise != null) {
            if (exercise.getMode().equals("running") || exercise.getMode().equals("cycling")) {
                setContentView(R.layout.activity_detail_tracked);
            } else {
                setContentView(R.layout.activity_detail_repetitions);
            }

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = findViewById(R.id.share_button);
            fab.setOnClickListener(this);
        }
        // Controller

    }

    @Override
    public void onClick(View v) {

    }
}

package com.uniks.myfit;

import android.content.Intent;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.uniks.myfit.controller.MapsController;
import com.uniks.myfit.database.AppDatabase;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, MainActivity.databaseName).allowMainThreadQueries().fallbackToDestructiveMigration().build();

        // View
        // TODO: set title based on the exercise type from stored data
        this.setTitle("Exercise");

        // TODO: also choose layout based on exercise type from stored data
        if (true) {
            setContentView(R.layout.activity_detail_tracked);
        } else {
            setContentView(R.layout.activity_detail_repetitions);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.share_button);
        fab.setOnClickListener(this);

        // Controllers
        /*MapsController mapsController = new MapsController(this);
        mapsController.init();*/



        // Model

    }

    @Override
    public void onClick(View v) {
        /* Share It with Google plus*/
        Intent mIntent =new Intent(Intent.ACTION_SEND);
        mIntent.setType("text/plain");
        String shareBody ="Google+ Share";
        String shareSub = "Google+";
        mIntent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
        mIntent.putExtra(Intent.EXTRA_TEXT,shareSub);
        startActivity(Intent.createChooser(mIntent,"Share Using"));

    }
}

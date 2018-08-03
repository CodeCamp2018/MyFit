package com.uniks.myfit;

import android.content.Intent;
import android.arch.persistence.room.Room;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;


import com.uniks.myfit.controller.MapsController;
import com.uniks.myfit.database.AppDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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




       /* This method will take screenshot from mobile screen*/
    public void takeAndSaveScreenShot()
    {

    }



    @Override
    public void onClick(View v) {
        /* Share Button*/
        Intent shareIntent =new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody ="Write your Body here";
        String shareSub = "Write your Subject here";
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
        shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
        startActivity(Intent.createChooser(shareIntent,"Share Using"));

        /* Call takeAndSaveScreenShot */

        takeAndSaveScreenShot();
    }
}

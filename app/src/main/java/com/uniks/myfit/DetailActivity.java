package com.uniks.myfit;

import android.content.Intent;
import android.arch.persistence.room.Room;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import com.uniks.myfit.controller.MapsController;
import com.uniks.myfit.database.AppDatabase;
import com.uniks.myfit.database.SportExercise;
import com.uniks.myfit.database.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

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

    }
       /* This method will take screenshot from mobile screen*/
    private Uri takeScreenShot() {
        // Get root View of your application
        View rootView = findViewById(R.id.map);

        // Enable drawing cache
        rootView.setDrawingCacheEnabled(true);

        // Create image
        Bitmap bitmap = rootView.getDrawingCache();
        // Save image in external storage
        Uri bmpUri=saveScreenShot(bitmap);
        return bmpUri;

    }

    /* This method will save screenshot taken by takeScreenShot method */
    public Uri  saveScreenShot(Bitmap bitmap) {
        Uri bmpUri = null;
        try {
            // Create ByteArrayOutputStream object to store bytes of compressed image
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            // Compress the image in jpeg format
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
            // Create FileOutputStream object to write image to external storage
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            // Close output file
            out.close();
            /* Store it in a file*/
            bmpUri = bmpUri.fromFile(file); // use this version for API >= 24

        } catch (FileNotFoundException e) {
            Log.e("Main", e.getMessage());
        } catch (IOException e) {
            Log.e("Main", e.getMessage(), e);
        }

        return bmpUri;
    }

    @Override
    public void onClick(View v) {
        Uri bmpUri=takeScreenShot();
        /* Share Button*/
         Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = "Write your Body here";
        String shareSub = "Write your Subject here";
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
        shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);shareIntent.setType("image/*");
        shareIntent.setType("image/*");
    }


}

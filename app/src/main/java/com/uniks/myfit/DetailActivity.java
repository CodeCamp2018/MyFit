package com.uniks.myfit;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.uniks.myfit.database.AppDatabase;
import com.uniks.myfit.database.LocationData;
import com.uniks.myfit.database.SportExercise;
import com.uniks.myfit.database.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

        int index = getIntent().getIntExtra("POSITION", 0);

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

        setExerciseData();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.share_button);
        fab.setOnClickListener(this);
        // Controller

    }

    private void setExerciseData() {
        TextView startTime = findViewById(R.id.exercise_started_time);
        TextView duration = findViewById(R.id.exercise_duration);
        // TODO: no endtime

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

    /* This method will take screenshot from mobile screen*/
    private Uri takeScreenShot() {

        // get bitmap
        View screenView = findViewById(R.id.map);
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);

        // store bitmap
        File mainDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyFit");
        if (!mainDir.exists()) {
            if(mainDir.mkdir()) {
                Log.e("Create Directory", "Main Directory Created: " + mainDir);
            }
        }

        File dir = new File(mainDir.getAbsolutePath());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(mainDir.getAbsolutePath(), "screenshot" + Calendar.getInstance().getTime().getTime() + ".jpg");
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Uri.fromFile(file);

    }

    /* This method will save screenshot taken by takeScreenShot method */
    public Uri saveScreenShot(Bitmap bitmap) {
        Uri bmpUri = null;
        try {
            // Create ByteArrayOutputStream object to store bytes of compressed image
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            // Compress the image in jpeg format
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
            // Create FileOutputStream object to write image to external storage
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".jpg");
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
        Uri bmpUri;
        String shareBody = null; // "Write your Body here";
        try {
            shareBody = getJsonFromExerciseData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String shareSub = "Exercise data from MyFit";
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("*/*");

        if (exercise.getMode() == 0 || exercise.getMode() == 1) {
            bmpUri = takeScreenShot();
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        }

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

        startActivity(Intent.createChooser(shareIntent, "Share Exercise Data"));

        /*
        // share
        Uri uri = Uri.fromFile(file);//Convert file path into Uri for sharing
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.sharing_text));
        intent.putExtra(Intent.EXTRA_STREAM, uri);//pass uri here
        startActivity(Intent.createChooser(intent, getString(R.string.share_title)));

        * */

        /*Uri imageUri = Uri.parse("android.resource://" + getPackageName()
        + "/drawable/" + "ic_launcher");
 Intent shareIntent = new Intent();
 shareIntent.setAction(Intent.ACTION_SEND);
 shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
 shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
 shareIntent.setType("image/jpeg");
 shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
 startActivity(Intent.createChooser(shareIntent, "send"));*/


    }

    private String getJsonFromExerciseData() throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("start", exercise.getDate());
        obj.put("duration", exercise.getTripTime());

        switch (exercise.getMode()) {

            case 0: // running
                List<LocationData> runningLocationData = db.locationDataDao().getAllFromExercise(exercise.getId());

                JSONArray runningLocationArray = new JSONArray(runningLocationData);

                obj.put("distance", exercise.getDistance());
                obj.put("steps", exercise.getAmountOfRepeats());
                obj.put("locationData", runningLocationArray);
                break;
            case 1: // cycling

                List<LocationData> cyclingLocationData = db.locationDataDao().getAllFromExercise(exercise.getId());

                JSONArray cyclingLocationArray = new JSONArray(cyclingLocationData);

                obj.put("distance", exercise.getDistance());
                obj.put("speed", exercise.getSpeed());
                obj.put("locationData", cyclingLocationArray);

                break;
            case 2: // pushups

                obj.put("pushUps", exercise.getAmountOfRepeats());

                break;
            case 3: // situps

                obj.put("sitUps", exercise.getAmountOfRepeats());

                break;

        }

        return obj.toString();
    }


   /* private void onSharedIntent() {
        Intent shareIntent = getIntent();// Receive intent
        String receivedAction = shareIntent.getAction();
        // Get type from receive intent
        String receivedType = shareIntent.getType();

        if (receivedAction.equals(Intent.ACTION_SEND))
        {
            if (receivedType.startsWith("text/"))
            {
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Share Text"));
            }else if (receivedType.startsWith("image/"))
            {
                shareIntent.setType("image/*");
                /* Share Button
                Uri bmpUri = takeScreenShot();
                startActivity(Intent.createChooser(shareIntent, "Share Image"));
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            }    }

        }*/

}
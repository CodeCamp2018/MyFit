package com.uniks.myfit;

import android.app.FragmentTransaction;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.MapFragment;
import com.uniks.myfit.controller.MapsController;
import com.uniks.myfit.model.AccTriple;

import java.util.ArrayList;
import java.util.Locale;

public class TrackingViewActivity extends AppCompatActivity {
    private int exerciseMode;
    private String customTitle = "Exercise";

    private ArrayList<Location> locationQueue;
    private ArrayList<AccTriple> accelerometerQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        exerciseMode = getIntent().getIntExtra("EXERCISE", 0);

        switch(exerciseMode) {
            case 0: customTitle = getString(R.string.running);
                    break;
            case 1: customTitle = getString(R.string.cycling);
                    break;
            case 2: customTitle = getString(R.string.pushups);
                    break;
            case 3: customTitle = getString(R.string.situps);
                    break;
        }

        locationQueue = new ArrayList<>();
        accelerometerQueue = new ArrayList<>();

        // TODO: set title based on the exercise type (and set it from @string resource)

        this.setTitle(customTitle);

        setContentView(R.layout.activity_tracking_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton stopButton = (ImageButton) findViewById(R.id.stop_button);
        stopButton.setOnClickListener(stopListener);

        //TODO: decide map creation based on chosen exercise
        //Insert map in our view

        if (exerciseMode <= 1) {
            MapsController mapsController = new MapsController(this);
        }
    }

    // handle tracking stop
    private View.OnClickListener stopListener = new View.OnClickListener() {
        public void onClick(View v) {
            //TODO: end tracking
        }
    };


    public ArrayList<Location> getLocationQueue() {
        return locationQueue;
    }

    public void setLocationQueue(ArrayList<Location> locationQueue) {
        this.locationQueue = locationQueue;
    }

    public ArrayList<AccTriple> getAccelerometerQueue() {
        return accelerometerQueue;
    }
}

package com.uniks.myfit;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.MapFragment;
import com.uniks.myfit.model.StepCounterService;

public class TrackingViewActivity extends AppCompatActivity {

    StepCounterService stepcounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: set title based on the exercise type (and set it from @string resource)
        this.setTitle("Exercising");

        setContentView(R.layout.activity_tracking_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton PlayPauseButton = (ImageButton) findViewById(R.id.play_pause_button);
        PlayPauseButton.setOnClickListener(playPauseListener);

        ImageButton StopButton = (ImageButton) findViewById(R.id.stop_button);
        PlayPauseButton.setOnClickListener(stopListener);

        //TODO: decide map creation based on chosen exercise
        //Insert map in our view
        MapFragment mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_container, mapFragment);
        fragmentTransaction.commit();
    }

    private void startStepCounting() {
        /* StepCounter Software Sensor*/
        stepcounter = new StepCounterService(this);
        /* Step Count Init*/
        stepcounter.onStart();
    }

    // handle playing and pausing tracking
    private View.OnClickListener playPauseListener = new View.OnClickListener() {
        public void onClick(View v) {
            //TODO: play/pause tracking

        }
    };

    // handle tracking stop
    private View.OnClickListener stopListener = new View.OnClickListener() {
        public void onClick(View v) {
            //TODO: end tracking
        }
    };


}

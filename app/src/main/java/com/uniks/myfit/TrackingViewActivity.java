package com.uniks.myfit;

import android.arch.persistence.room.Room;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.uniks.myfit.controller.MapsController;
import com.uniks.myfit.controller.PushupCtrl;
import com.uniks.myfit.controller.SitUpsCtrl;
import com.uniks.myfit.database.AppDatabase;
import com.uniks.myfit.model.AccTripleVec;
import com.uniks.myfit.model.StepCounterService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TrackingViewActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_FINE_LOCATION = 351;
    private static final int MIN_NUMBER_OF_ELEMENTS = 10;
    private SitUpsCtrl sitUpsCtrl;
    private PushupCtrl pushupCtrl;
    private StepCounterService stepCounterService;
    private MapsController mapsController;

    private int exerciseMode;
    private boolean activeStateMachine;
    private int actualState;
    private Date startExercisingTime;
    private String customTitle = "Exercise";

    private ArrayList<Location> locationQueue;
    private ArrayList<AccTripleVec> accelerometerQueue;

    public AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, MainActivity.databaseName).allowMainThreadQueries().fallbackToDestructiveMigration().build();

        sitUpsCtrl = new SitUpsCtrl(this);
        stepCounterService = new StepCounterService(this);
        mapsController = new MapsController(this);

        activeStateMachine = true;
        actualState = 0;
        startExercisingTime = Calendar.getInstance().getTime();

        locationQueue = new ArrayList<>();
        accelerometerQueue = new ArrayList<>();

        exerciseMode = getIntent().getIntExtra("EXERCISE", 0);

        switch (exerciseMode) {
            case 0:
                customTitle = getString(R.string.running);
                break;
            case 1:
                customTitle = getString(R.string.cycling);
                break;
            case 2:
                customTitle = getString(R.string.pushups);
                break;
            case 3:
                customTitle = getString(R.string.situps);
                break;
        }

        locationQueue = new ArrayList<>();
        accelerometerQueue = new ArrayList<>();

        // set title based on the exercise type
        this.setTitle(customTitle);

        setContentView(R.layout.activity_tracking_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton stopButton = (ImageButton) findViewById(R.id.stop_button);
        stopButton.setOnClickListener(this);

        // start sensors
        startSensors();

        // start the state machine
        startStateMachine();
    }

    private void startSensors() {
        switch (exerciseMode) {
            case 0: // running

                stepCounterService.onStart();

                // set headlines
                includeMap();
                // distance
                TextView runningDistanceTitleUI = findViewById(R.id.title_1);
                runningDistanceTitleUI.setText(getResources().getString(R.string.distanceHeadline));
                // steps
                TextView runningStepCounterTitleUI = findViewById(R.id.title_2);
                runningStepCounterTitleUI.setText(getResources().getString(R.string.stepsHeadline));

                break;
            case 1: // cycling

                // set headlines
                includeMap();
                // distance
                TextView cyclingDistanceTitleUI = findViewById(R.id.title_1);
                cyclingDistanceTitleUI.setText(getResources().getString(R.string.distanceHeadline));
                // speed
                TextView cyclingSpeedTitleUI = findViewById(R.id.title_2);
                cyclingSpeedTitleUI.setText(getResources().getString(R.string.currentSpeedHeadline));

                break;
            case 2: // pushups

                // TODO: init pushupCtrl here
                pushupCtrl.proximityInit();

                // set headlines
                // count
                TextView pushupCountTitleUI = findViewById(R.id.title_1);
                pushupCountTitleUI.setText(getResources().getString(R.string.countHeadline));

                break;
            case 3: // situps

                sitUpsCtrl.init();

                // set headlines
                // count
                TextView situpCountTitleUI = findViewById(R.id.title_1);
                situpCountTitleUI.setText(getResources().getString(R.string.countHeadline));

                break;
        }

        // time
        TextView durationTitleUI = findViewById(R.id.title_3);
        durationTitleUI.setText(getResources().getString(R.string.timeHeadline));
    }

    private void startStateMachine() {
        // start processing-Thread who also updates UI-elements
        new Thread(new Runnable() {
            public void run() {

                while (activeStateMachine) {
                    switch (actualState) {
                        case 0: // wait-state
                            if (isThereEnoughData()) {
                                actualState = 1;
                            }
                            break;
                        case 1: // processing-state

                            processSensorData();

                            actualState = 0;
                            break;
                    }
                }
            }
        }).start();

    }

    private boolean isThereEnoughData() {
        boolean enough = false;
        switch (exerciseMode) {
            case 0: case 1:// running and cycling
                if (mapsController.getLinePoints().size() >= MIN_NUMBER_OF_ELEMENTS) {
                    enough = true;
                }
                break;
            case 2: // pushups

                break;
            case 3: // situps
                if (getAccelerometerQueue().size() >= MIN_NUMBER_OF_ELEMENTS) {
                    enough = true;
                }
                break;
        }

        return enough;
    }

    private void processSensorData() {
        String duration = getFormattedCurrentDuration();
        switch (exerciseMode) {
            case 0: // running
                int stepsCounted = stepCounterService.getActualCount();
                // TODO: do the measuring of distance from mapsController

                // set view - show distance, steps

                /* ------ example for calling ui-thread
                final Bitmap bitmap =
                        processBitMap("image.png");
                mImageView.post(new Runnable() {
                    public void run() {
                        mImageView.setImageBitmap(bitmap);
                    }
                });*/

                // distance
                TextView runningDistanceValueUI = findViewById(R.id.value_1);

                runningDistanceValueUI.setText(String.valueOf(mapsController.getTotalDistance()));
                // TODO: fill UI-Element here
                //steps
                TextView stepCounterValueUI = findViewById(R.id.value_2);
                stepCounterValueUI.setText(String.valueOf(stepsCounted));

                break;
            case 1: // cycling
                // TODO: get the measured traveling distance from mapsController
                // TODO: get the measured current speed from mapsController

                // set view - show distance, speed
                // distance
                TextView cyclingDistanceValueUI = findViewById(R.id.value_1);
                // TODO: fill UI-Element
                // speed
                TextView cyclingSpeedValueUI = findViewById(R.id.value_2);
                // TODO: fill UI-Element
                break;
            case 2: // pushups
                // TODO: get the count of pushups from pushupController

                // set view - show count
                TextView pushupCountValueUI = findViewById(R.id.value_1);
                // TODO: fill UI-Element
                break;
            case 3: // situps

                int situpCount = sitUpsCtrl.calculateSitups();

                // set view - show count
                TextView situpCountValueUI = findViewById(R.id.value_1);
                situpCountValueUI.setText(String.valueOf(situpCount));
                break;
        }

        // set time in UI
        TextView runningTimeValueUI = findViewById(R.id.value_3);
        runningTimeValueUI.setText(duration);
    }

    public ArrayList<Location> getLocationQueue() {
        return locationQueue;
    }

    public ArrayList<AccTripleVec> getAccelerometerQueue() {
        return accelerometerQueue;
    }

    private void includeMap() {
        //Insert map in our view
        if (exerciseMode <= 1) {
            mapsController = new MapsController(this);
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.map_container, mapsController.mapFragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * onClickListener for CloseBtn
     */
    @Override
    public void onClick(View v) {
        //end tracking
        activeStateMachine = false;

        sitUpsCtrl.stop();


        // TODO save data to database
        Date now = Calendar.getInstance().getTime();
        long exerciseDuration = now.getTime() - startExercisingTime.getTime(); // TODO: save to db

        // TODO switch back to main screen
    }

    public String getFormattedCurrentDuration() {
        Date now = Calendar.getInstance().getTime();
        long duration = now.getTime() - startExercisingTime.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        long hours = duration / hoursInMilli;
        long minutes = duration / minutesInMilli;
        long seconds = duration / secondsInMilli;

        return MessageFormat.format("{0}:{1}:{2}", hours, minutes, seconds);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapsController.startLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

    @Override
    protected void onDestroy() {

        // TODO: handle pressing "back"-Btn the same way as the user presses "stop"-Btn

        super.onDestroy();

    }
}

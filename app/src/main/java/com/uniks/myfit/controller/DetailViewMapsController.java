package com.uniks.myfit.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.uniks.myfit.DetailActivity;
import com.uniks.myfit.database.AppDatabase;
import com.uniks.myfit.database.LocationData;
import com.uniks.myfit.database.SportExercise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailViewMapsController extends FragmentActivity implements OnMapReadyCallback, GoogleMap.SnapshotReadyCallback {

    private DetailActivity detailActivity;
    private GoogleMap map;
    private SportExercise exercise;
    private AppDatabase db;
    private Polyline polyline;
    private List<LocationData> allLocation;

    public DetailViewMapsController(DetailActivity detailActivity, SportExercise exercise, AppDatabase db) {
        this.detailActivity = detailActivity;
        this.exercise = exercise;
        this.db = db;
        allLocation = db.locationDataDao().getAllFromExercise(exercise.getId());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.e("DetailMapsController", "Map is ready!");

        map = googleMap;

        // draw the path the user traveled
        List<LatLng> tmp = new ArrayList<>();
        for (LocationData ld :
                allLocation) {
            tmp.add(ld.getLatLng());
        }

        polyline = map.addPolyline(new PolylineOptions().color(0xff0564ff));
        polyline.setPoints(tmp);

        if (allLocation.size() > 0) {
            // zoom to it
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(allLocation.get(0).getLatLng(), 15));
            map.addMarker(new MarkerOptions().position(allLocation.get(0).getLatLng())).setVisible(true);
            map.addMarker(new MarkerOptions().position(allLocation.get(allLocation.size() - 1).getLatLng())).setVisible(true);
        }
    }

    public void doMapScreenshot() {
        map.snapshot(this);
    }

    @Override
    public void onSnapshotReady(Bitmap bitmap) {

        File mainDir = new File(detailActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyFit");
        if (!mainDir.exists()) {
            if (mainDir.mkdir()) {
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
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // share
        String shareBody = null;
        try {
            shareBody = getNiceConclusionTxt();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String shareSub = "Exercise data from MyFit";
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

        detailActivity.startActivity(Intent.createChooser(shareIntent, "Share Exercise Data"));

    }

    private String getNiceConclusionTxt() throws JSONException {

        List<LocationData> locationData = db.locationDataDao().getAllFromExercise(exercise.getId());

        SimpleDateFormat dateSdf = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat timeSdf = new SimpleDateFormat("hh:mm");

        String message = "Hey there!\nI did some exercise on " + dateSdf.format(exercise.getDate()) + " at " + timeSdf.format(exercise.getDate()) + " for " + exercise.getTripTime() + " hours. Look what I did: ";

        JSONObject obj = new JSONObject();
        obj.put("start", exercise.getDate());
        obj.put("duration", exercise.getTripTime());

        String addition = "";


        switch (exercise.getMode()) {

            case 0: // running

                addition = "I ran " + String.format("%.2f", exercise.getDistance()) + " km and did " + exercise.getAmountOfRepeats() + " steps.\n";

                JSONArray runningLocationArray = new JSONArray(locationData);

                obj.put("distance", exercise.getDistance());
                obj.put("steps", exercise.getAmountOfRepeats());
                obj.put("locationData", runningLocationArray);
                break;
            case 1: // cycling

                addition = "I cycled " + String.format("%.2f", exercise.getDistance()) + " km with a max speed of " + exercise.getSpeed() + " km/h.\n";

                JSONArray cyclingLocationArray = new JSONArray(locationData);

                obj.put("distance", exercise.getDistance());
                obj.put("speed", exercise.getSpeed());
                obj.put("locationData", cyclingLocationArray);

                break;

        }


        StringBuilder route = new StringBuilder("These are the coordinates of my route:\n");

        for (int i = 0; i < locationData.size(); i++) {
            route.append(locationData.get(i).getLatitude()).append(", ").append(locationData.get(i).getLongitude());
            if (i != locationData.size() - 1) {
                // last element
                route.append("\n");
            }
        }


        addition += route;


        return message + addition;
    }
}

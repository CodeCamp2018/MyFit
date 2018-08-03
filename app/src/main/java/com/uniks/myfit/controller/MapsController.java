
package com.uniks.myfit.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.TypedValue;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.uniks.myfit.DetailActivity;
import com.uniks.myfit.MainActivity;
import com.uniks.myfit.R;
import com.uniks.myfit.TrackingViewActivity;
import com.uniks.myfit.database.LocationData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class MapsController implements OnMapReadyCallback, LocationListener {
    public SupportMapFragment mapFragment;

    public Location location;

    private GoogleMap mMap;
    private int padding;
    //class type-which manages the location
    private LocationManager locationManager;
    private TrackingViewActivity trackingViewActivity;

    private boolean firstLocation = true;
    private Polyline polyline;

    private ArrayList<LocationData> linePoints = new ArrayList<>();

    public MapsController(TrackingViewActivity trackingViewActivity) {
        this.trackingViewActivity = trackingViewActivity;
        this.padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96, trackingViewActivity.getResources().getDisplayMetrics());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);
    }

    public void init() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setPadding(0,padding,0,0);

        //intialize the location manager
        locationManager = (LocationManager) trackingViewActivity.getSystemService(LOCATION_SERVICE);

        // ask for location permissions if not granted already
        if (ActivityCompat.checkSelfPermission(trackingViewActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(trackingViewActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    trackingViewActivity.REQUEST_FINE_LOCATION );

            return;
        }

        startLocation();

    }

    @SuppressLint("MissingPermission")
    public void startLocation() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        this.location = location;

        //get the latitude and longitude
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        if (firstLocation) {
            // mark the starting point
            mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Starting Point"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10.2f));

            polyline = mMap.addPolyline(new PolylineOptions().add(userLocation).color(0xff0564ff));

            linePoints.add(new LocationData(userLocation.latitude, userLocation.longitude));

            firstLocation = false;
        } else {
            // track path

            linePoints.add(new LocationData(userLocation.latitude, userLocation.longitude));
            List<LatLng> tmp = new ArrayList<>();

            for (LocationData curr :
                    linePoints) {
                tmp.add(curr.getLatLng());
            }

            polyline.setPoints(tmp);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public int getSpeed() {
        if (location == null) {
            return 0;
        }

        return (int) (3.6 * location.getSpeed()); // speed in km/h
    }

    public List getPath() {
        return linePoints;
    }

    public void stopTracking() {
        locationManager.removeUpdates(this);
    }

    private double twoPointDistance(LocationData pointOne, LocationData pointTwo) {
        double R = 6371f; // Radius of the earth in km
        double dLat = (pointOne.getLatitude() - pointTwo.getLatitude()) * Math.PI / 180f;
        double dLon = (pointOne.getLongitude() - pointTwo.getLongitude()) * Math.PI / 180f;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(pointOne.getLatitude() * Math.PI / 180f) * Math.cos(pointTwo.getLatitude() * Math.PI / 180f) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2f * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d;
    }

    public double getTotalDistance() {

        if (linePoints.size() < 2) {
            return 0;
        }

        double totalDistance = 0;
        LocationData prevElement = new LocationData(0, 0);

        for (LocationData currElement: linePoints) {
            if (prevElement.getLongitude() == 0 && prevElement.getLatitude() == 0) { // NOT for Santa
                prevElement = currElement;
                continue;
            }

            totalDistance += twoPointDistance(currElement, prevElement);

            prevElement = currElement;
        }

        return totalDistance;
    }

    public ArrayList<LocationData> getLinePoints() {
        return linePoints;
    }
}


/**
 * Manipulates the map once available.
 * This callback is triggered when the map is ready to be used.
 * This is where we can add markers or lines, add listeners or move the camera. In this case,
 * we just add a marker near Sydney, Australia.
 * If Google Play services is not installed on the device, the user will be prompted to install
 * it inside the SupportMapFragment. This method will only be triggered once the user has
 * installed Google Play services and returned to the app.
 */

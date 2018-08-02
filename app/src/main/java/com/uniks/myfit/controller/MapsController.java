
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class MapsController implements OnMapReadyCallback, LocationListener {
    public SupportMapFragment mapFragment;
    private GoogleMap mMap;
    //class type-which manages the location
    private LocationManager locationManager;
    private TrackingViewActivity trackingViewActivity;

    private boolean firstLocation = true;
    private PolylineOptions polylineOptions;
    private Polyline polyline;
    private List linePoints = new ArrayList();

    public MapsController(TrackingViewActivity trackingViewActivity) {
        this.trackingViewActivity = trackingViewActivity;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mapFragment = (SupportMapFragment) SupportMapFragment.newInstance(); // TODO
        mapFragment.getMapAsync(this);
    }

    public void init() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //intialing the location manager
        locationManager = (LocationManager) trackingViewActivity.getSystemService(LOCATION_SERVICE);
        //asking for the marker
        // ask user for the location after certian time & distance
        if (ActivityCompat.checkSelfPermission(trackingViewActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(trackingViewActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(trackingViewActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, trackingViewActivity.REQUEST_FINE_LOCATION );
            // TODO:
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            startLocation();
        }

    }

    @SuppressLint("MissingPermission")
    public void startLocation() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //get the latitude and longitude
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng userLocation = new LatLng(latitude, longitude);

        if (firstLocation) {
            mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Starting Point"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10.2f));
            polylineOptions = new PolylineOptions().add(userLocation);
            polyline = mMap.addPolyline(polylineOptions);

            linePoints.add(0, userLocation);

            firstLocation = false;
        } else {
            linePoints.add(userLocation);

            polyline.setPoints(linePoints);

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

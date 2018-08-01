
package com.uniks.myfit.controller;

import android.Manifest;
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
import com.uniks.myfit.DetailActivity;
import com.uniks.myfit.MainActivity;
import com.uniks.myfit.R;
import com.uniks.myfit.TrackingViewActivity;

import java.io.IOException;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class MapsController implements OnMapReadyCallback {

    private GoogleMap mMap;
    //class type-which manages the location
    LocationManager locationManager;
    TrackingViewActivity trackingViewActivity;
   // private Object googleMap;

    public MapsController(TrackingViewActivity trackingViewActivity) {
        this.trackingViewActivity = trackingViewActivity;
    }

    public void init() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) trackingViewActivity.getSupportFragmentManager()
                .findFragmentById(R.id.map); // TODO
        mapFragment.getMapAsync(this);
        //intialing the location manager
        locationManager = (LocationManager) trackingViewActivity.getSystemService(LOCATION_SERVICE);
        //asking for the marker
        // ask user for the location after certian time & distance
        if (ActivityCompat.checkSelfPermission(trackingViewActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(trackingViewActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } //this method only works if we have network provider & we are also checking that do we have a network provider
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    trackingViewActivity.getLocationQueue().add(location);

                    //get the latitude
                    double latitude = location.getLatitude();
                    //get the longitude
                    double longitude = location.getLongitude();
                    //instance latitude & Longitude class
                    //Navigation Toool for the map
                    double lat=latitude;
                    double lng=longitude;
                    String format="geo:0,0?q"+lat+","+lng+"(str)";
                    Uri uri=Uri.parse(format);
                    Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    trackingViewActivity.startActivity(intent);
                    LatLng latLng = new LatLng(latitude, longitude);
                    LatLng userLocation=new LatLng(location.getLatitude(),location.getLatitude());
                    mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Starting Point"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,10.2f));
                    //instantiate the class,Geocoder
                    Geocoder geocoder = new Geocoder(trackingViewActivity.getApplicationContext());
                    try {
                        List<Address> adressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str = adressList.get(0).getLocality() + ",";
                        str += adressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }


                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }

            });
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //get the latitude
                    double latitude = location.getLatitude();
                    //get the longitude
                    double longitude = location.getLongitude();
                    //instance latitude & Longitude class
                    //Navigation Toool for the map
                    double lat=latitude;
                    double lng=longitude;
                    String format="geo:0,0?q"+lat+","+lng+"(str)";
                    Uri uri=Uri.parse(format);
                    Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    trackingViewActivity.startActivity(intent);
                    LatLng latLng = new LatLng(latitude, longitude);
                    LatLng userLocation=new LatLng(location.getLatitude(),location.getLatitude());
                    mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Starting Point"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,10.2f));
                    //instantiate the class,Geocoder
                    Geocoder geocoder = new Geocoder(trackingViewActivity.getApplicationContext());
                    try {
                        List<Address> adressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str = adressList.get(0).getLocality() + ",";
                        str += adressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//                LatLng sydney = new LatLng(-34, 151);
//                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10.2f));
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

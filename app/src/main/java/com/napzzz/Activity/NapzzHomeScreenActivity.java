package com.napzzz.Activity;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.napzzz.R;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



/**
 * Created by koizen12 on 8/26/15.
 */
public class NapzzHomeScreenActivity extends FragmentActivity implements LocationListener,OnMapReadyCallback {

    GoogleMap googleMap;
    ImageView mHeaderLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //show error dialog if GoolglePlayServices not available
       /* if (!isGooglePlayServicesAvailable()) {
            finish();
        }*/
        setContentView(R.layout.napzz_home_screen);

        mHeaderLogo = (ImageView) findViewById(R.id.img_napzzLogo);

        mHeaderLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mHeaderLogoIntent = new Intent(NapzzHomeScreenActivity.this, NapzzFilterScreenActivity.class);
                startActivity(mHeaderLogoIntent);

            }
        });

   /*     try {
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);

            supportMapFragment.getMapAsync(this);
           *//* googleMap = supportMapFragment.getMap();
            googleMap.setMyLocationEnabled(true);
            googleMap.getMyLocation();*//*
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

     *//*       Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_HIGH);
            String bestProvider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);*//*
        }
        catch (Exception e){
            e.printStackTrace();
        }*/
    }

    @Override
    public void onLocationChanged(Location location) {
        /*double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        Toast.makeText(getApplicationContext(),"Latitude:" + latitude + ", Longitude:" + longitude,Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    Log.i("lat:" + lat, "long :" + lon);
                    LatLng latLng = new LatLng(lat, lon);
                    NapzzHomeScreenActivity.this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    NapzzHomeScreenActivity.this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                }
            }
        });

    }
}
package com.napzzz.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.napzzz.R;
import com.napzzz.Utils.Utility;

import org.json.JSONObject;

public class MainActivity extends Activity implements View.OnClickListener {

    Button hostingBtn, napperzBtn, bookingBtn, filtersBtn, whyHostBtn, listYourSpaceBtn;
    LinearLayout bookingFiltersLayout, hostListLayout;
    //GoogleMap googleMap;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hostingBtn = (Button) findViewById(R.id.hostingBtn);
        napperzBtn = (Button) findViewById(R.id.napperzBtn);

        bookingBtn = (Button) findViewById(R.id.bookingBtn);
        filtersBtn = (Button) findViewById(R.id.filtersBtn);
        whyHostBtn = (Button) findViewById(R.id.whyHostBtn);
        listYourSpaceBtn = (Button) findViewById(R.id.listYourSpaceBtn);

        bookingFiltersLayout = (LinearLayout) findViewById(R.id.bookingFiltersLayout);
        hostListLayout = (LinearLayout) findViewById(R.id.hostListLayout);

        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.theme_color)));
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'> Napzzz</font>"));

       /* googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        googleMap.setTrafficEnabled(true);
        // Changing map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        googleMap.setMyLocationEnabled(true);
        googleMap.getMinZoomLevel();*/

        hostingBtn.setOnClickListener(this);
        napperzBtn.setOnClickListener(this);

        bookingBtn.setOnClickListener(this);
        filtersBtn.setOnClickListener(this);
        whyHostBtn.setOnClickListener(this);
        listYourSpaceBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.hostingBtn:
                hostingBtn.setBackgroundColor(getResources().getColor(R.color.theme_color));
                napperzBtn.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                hostListLayout.setVisibility(View.VISIBLE);
                bookingFiltersLayout.setVisibility(View.GONE);
                break;

            case R.id.napperzBtn:
                hostingBtn.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                napperzBtn.setBackgroundColor(getResources().getColor(R.color.theme_color));
                bookingFiltersLayout.setVisibility(View.VISIBLE);
                hostListLayout.setVisibility(View.GONE);
                break;

            case R.id.bookingBtn:

                Toast.makeText(getApplicationContext(), "Booking", Toast.LENGTH_SHORT).show();
                break;
            case R.id.filtersBtn:
                Toast.makeText(getApplicationContext(), "Filters", Toast.LENGTH_SHORT).show();
                break;
            case R.id.whyHostBtn:
                Toast.makeText(getApplicationContext(), "Why Host", Toast.LENGTH_SHORT).show();
                break;
            case R.id.listYourSpaceBtn:
                Intent i = new Intent(MainActivity.this, ListYourSpaceActivity.class);
                startActivity(i);
                break;

        }
        return;
    }
}
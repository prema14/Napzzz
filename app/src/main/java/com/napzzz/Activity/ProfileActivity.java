package com.napzzz.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.napzzz.Api.AsyncApi;
import com.napzzz.R;
import com.napzzz.Utils.Utility;

import org.json.JSONObject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by pramod on 8/26/15.
 */
public class ProfileActivity extends Activity {
    Button mBtnBecomeHost, mBtnBooking;
    Handler mHandler = new Handler();
    RequestQueue queue;
    ImageView mLeftHeaderIcon, mUser_profile_image, mDoller, mAt_icon, mPhone, mProfile;
    TextView mUser_profile_name, mUser_profile_description, mUser_profile_show_review, mUser_birthday_value,
            mUser_email_value, mUser_phone_value, mUser_language_value;
    RatingBar mUser_profile_ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_profile);
        mBtnBecomeHost = (Button) findViewById(R.id.btn_become_host);
        mBtnBooking = (Button) findViewById(R.id.btn_booking);
        mLeftHeaderIcon = (ImageView) findViewById(R.id.btn_back_header_profile);
        mUser_profile_ratingBar = (RatingBar) findViewById(R.id.user_profile_ratingBar);

        mUser_profile_ratingBar.setIsIndicator(true);
        mUser_profile_ratingBar.setRating(1.6f);
        LayerDrawable stars = (LayerDrawable) mUser_profile_ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        //   hitOnServer(ProfileActivity.this, AsyncApi.SAMPLE_API_TESTING);

        mBtnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                BookingProfileFragnment mFragment = new BookingProfileFragnment();
                fragmentTransaction.replace(R.id.top_container, mFragment, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        mBtnBecomeHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to BecomeHost page.
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                BecomeHostProfileFragment mFragment = new BecomeHostProfileFragment();
                fragmentTransaction.replace(R.id.top_container, mFragment, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        mLeftHeaderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() == 0) {
                    finish();
                } else {
                    getFragmentManager().popBackStack();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    // for server hit using volley.
    public void hitOnServer(Context mContext, final String URL) {
        if (Utility.checkIntenetConnectivity(mContext)) {
            try {

                Map<String, String> requestMap = new TreeMap<>();
                requestMap.put("name" , "name");
                requestMap.put("password", "112452");

                queue = ApplicationController.getInstance().getRequestQueue();
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response != null) {
                                    Log.i("server response :", ":" + response);
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(
                            VolleyError error) {
                    }
                });
                //stop previous request on server.
                //fragment.setTimeOut(jsObjRequest);
                queue.add(jsObjRequest);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void serverRequest(){

        String url = AsyncApi.SAMPLE_API;

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        queue = ApplicationController.getInstance().getRequestQueue();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                       Log.d("JSON RESPONSE :", response.toString());
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "fetch");
                return params;
            }

        };

// Adding request to request queue
        queue.add(jsonObjReq);

    }
}


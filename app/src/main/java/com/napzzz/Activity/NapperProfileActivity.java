package com.napzzz.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.napzzz.Api.AsyncApi;
import com.napzzz.MobileVarify.MobileVarificationActivity;
import com.napzzz.Models.NapperClassItems;
import com.napzzz.NetverifyId.SampleActivity;
import com.napzzz.R;
import com.napzzz.Utils.Globals;
import com.napzzz.Utils.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pramod on 8/26/15.
 */
public class NapperProfileActivity extends Activity {
    //Handler mHandler = new Handler();
    RequestQueue queue;
    public String tempImageURL = "http://cdn.screenrant.com/wp-content/uploads/A-Stormtrooper-lost-in-the-desert.-2560x1080-Imgur.jpg";
    ImageLoader mImageLoader;
    ImageView mNapperLeftHeaderIcon, mNapperUser_profile_image, mNapperDoller, mNapperAt_icon, mNapperPhone, mNapperProfile;
    TextView mNapperUser_profile_name, mNapperUser_profile_description, mNapperUser_profile_show_review,
            mNapperUser_birthday_value, mNapperUser_language_value;
    RatingBar mNapperUser_profile_ratingBar;
    public String REQUEST_TYPE_SERVER_ACTION = "action";
    public String REQUEST_TYPE_SERVER_REQUEST_USER_ID = "user_id";
    public String REQUEST_TYPE_SERVER_ACCESS_TOKAN = "accessToken";

    public String USER_ACTION_TYPE = "fetch";
    public String USER_ID = "17";
    public String ACCESS_TOKAN = "6c9cfe542dfff00bffa9dfd52ed536b0";

    Handler mHandler = new Handler();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_napper_profile);

        mNapperLeftHeaderIcon = (ImageView) findViewById(R.id.napper_btn_back_header_profile);
        mNapperUser_profile_ratingBar = (RatingBar) findViewById(R.id.napper_user_profile_ratingBar);

        mNapperUser_profile_image = (ImageView) findViewById(R.id.napper_user_profle_image);
        mNapperPhone = (ImageView) findViewById(R.id.napper_phone);
        mNapperUser_profile_name = (TextView) findViewById(R.id.napper_user_profile_name);
        mNapperProfile = (ImageView) findViewById(R.id.napper_profile);

        mNapperUser_birthday_value = (TextView) findViewById(R.id.napper_user_birthday_value);
        mNapperUser_language_value = (TextView) findViewById(R.id.napper_user_language_value);


        mNapperUser_profile_ratingBar.setIsIndicator(true);
        mNapperUser_profile_ratingBar.setRating(1.6f);
        LayerDrawable stars = (LayerDrawable) mNapperUser_profile_ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        mNapperLeftHeaderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() == 0) {
                    finish();
                } else {
                    getFragmentManager().popBackStack();
                }

            }
        });

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                hitOnServer(getApplication(), AsyncApi.PROFILE_API);
            }
        });

        mNapperProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentvarify = new Intent(NapperProfileActivity.this, SampleActivity.class);
                startActivity(intentvarify);
            }
        });

        mNapperPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentvarify = new Intent(NapperProfileActivity.this, MobileVarificationActivity.class);
                startActivity(intentvarify);
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


               /* JSONObject obj = new JSONObject();
                try {
                    obj.put(REQUEST_TYPE_SERVER_ACTION, USER_ACTION_TYPE);
                   // obj.put(REQUEST_TYPE_SERVER_REQUEST_USER_ID, USER_ID);
                   // obj.put(REQUEST_TYPE_SERVER_ACCESS_TOKAN, ACCESS_TOKAN);
                }
                catch (Exception e){
                    e.printStackTrace();
                }*/

                String extraParams = "?" +REQUEST_TYPE_SERVER_ACTION + "=" + USER_ACTION_TYPE + "&"
                        + REQUEST_TYPE_SERVER_REQUEST_USER_ID + "=" +USER_ID + "&" + REQUEST_TYPE_SERVER_ACCESS_TOKAN
                        + "=" + ACCESS_TOKAN;

                queue = ApplicationController.getInstance().getRequestQueue();
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, URL+ extraParams, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response != null) {
                                    Log.i("server response :", ":" + response);
                                    try {

                                        ArrayList<NapperClassItems> napperProfileArray = new ArrayList<NapperClassItems>();
                                        if (response.has(Globals.REQUEST_KEY_MESSAGE)) {
                                            String message = null;
                                            Object msg = response.get(Globals.REQUEST_KEY_MESSAGE);
                                            message = msg.toString();
                                            Log.i("msg " , " :"  +message);
                                        }

                                        if (response.has(Globals.REQUEST_KEY_STATUS)) {
                                            String status = null;
                                            Object jsonStatus = response.get(Globals.REQUEST_KEY_STATUS);
                                            status = jsonStatus.toString();
                                            Log.i("status " , " :"  + status);
                                        }

                                        if (response.has(Globals.REQUEST_KEY_DATA)) {
                                            Object dataJsonObject = response.get(Globals.REQUEST_KEY_DATA);
                                            Log.i("data " , " :"  + dataJsonObject);

                                            if(dataJsonObject != null){
                                                JSONObject dataObject = response.getJSONObject(Globals.REQUEST_KEY_DATA);
                                                String user_id = null;
                                                String userName;
                                                String userImage;
                                                String userEmail;
                                                String userGender;
                                                String userDOB;
                                                String userPhone;
                                                String userLanguage;
                                                String emailVarified;
                                                String mobileVerified;
                                                String totalRating;
                                                String totalReviews;

                                                if (dataObject.has(Globals.REQUEST_KEY_USER_ID)) {

                                                    Object user_id_obj = dataObject.get(Globals.REQUEST_KEY_USER_ID);
                                                    user_id = user_id_obj.toString();
                                                    Log.i("user_id " , " :"  + user_id);
                                                }
                                                if (dataObject.has(Globals.REQUEST_KEY_YOUR_NAME)) {

                                                    Object user_name_obj = dataObject.get(Globals.REQUEST_KEY_YOUR_NAME);
                                                    userName = user_name_obj.toString();
                                                    Log.i("userName " , " :"  + userName);
                                                    mNapperUser_profile_name.setText(userName);
                                                }
                                                if (dataObject.has(Globals.REQUEST_KEY_YOUR_EMAIL_ID)) {

                                                    Object user_email_obj = dataObject.get(Globals.REQUEST_KEY_YOUR_EMAIL_ID);
                                                    userEmail = user_email_obj.toString();
                                                    Log.i("user_email ", " :" + userEmail);
                                                }
                                                if (dataObject.has(Globals.REQUEST_KEY_GENDER)) {

                                                    Object user_gender_obj = dataObject.get(Globals.REQUEST_KEY_GENDER);
                                                    userGender = user_gender_obj.toString();
                                                    Log.i("userGender " , " :"  + userGender);
                                                }
                                                if (dataObject.has(Globals.REQUEST_KEY_MOBILE_NUMBER)) {

                                                    Object user_phone_obj = dataObject.get(Globals.REQUEST_KEY_MOBILE_NUMBER);
                                                    userPhone = user_phone_obj.toString();
                                                    Log.i("userPhone " , " :"  + userPhone);
                                                }
                                                if (dataObject.has(Globals.REQUEST_KEY_LANGUAGE)) {

                                                    Object user_language_obj = dataObject.get(Globals.REQUEST_KEY_LANGUAGE);
                                                    userLanguage = user_language_obj.toString();
                                                    Log.i("userLanguage " , " :"  + userLanguage);
                                                    mNapperUser_language_value.setText(userLanguage);
                                                }
                                                if (dataObject.has(Globals.REQUEST_KEY_EMAIL_VARIFIED)) {

                                                    Object user_email_varified_obj = dataObject.get(Globals.REQUEST_KEY_EMAIL_VARIFIED);
                                                    emailVarified = user_email_varified_obj.toString();
                                                    Log.i("emailVarified ", " :" + emailVarified);
                                                }
                                                if (dataObject.has(Globals.REQUEST_KEY_MOBILE_VARIFIED)) {

                                                    Object user_mobile_varif_obj = dataObject.get(Globals.REQUEST_KEY_MOBILE_VARIFIED);
                                                    mobileVerified = user_mobile_varif_obj.toString();
                                                    Log.i("mobileVerified " , " :"  + mobileVerified);
                                                }
                                                if (dataObject.has(Globals.REQUEST_KEY_TOTAL_RATING)) {

                                                    Object user_rating_obj = dataObject.get(Globals.REQUEST_KEY_TOTAL_RATING);
                                                    totalRating = user_rating_obj.toString();
                                                    Log.i("totalRating " , " :"  + totalRating);
                                                }
                                                if (dataObject.has(Globals.REQUEST_KEY_TOTAL_REVIEWS)) {

                                                    Object user_reviews_obj = dataObject.get(Globals.REQUEST_KEY_TOTAL_REVIEWS);
                                                    totalReviews = user_reviews_obj.toString();
                                                    Log.i("totalReviews " , " :"  + totalReviews);
                                                }

                                                if (dataObject.has(Globals.REQUEST_KEY_USER_DOB)) {

                                                    Object user_dob_obj = dataObject.get(Globals.REQUEST_KEY_USER_DOB);
                                                    userDOB = user_dob_obj.toString();
                                                    Log.i("userDOB " , " :"  + userDOB);
                                                    mNapperUser_birthday_value.setText(userDOB);
                                                }

                                                if (dataObject.has(Globals.REQUEST_KEY_PROFILE_PICTURE)) {

                                                    Object user_image_obj = dataObject.get(Globals.REQUEST_KEY_PROFILE_PICTURE);
                                                    userImage = user_image_obj.toString();
                                                    Log.i("userImage ", " :" + userImage);
                                                    if(userImage != null && !userImage.isEmpty()) {
                                                        DisplayImageOptions options = new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(50)) //rounded corner bitmap
                                                                .cacheInMemory(true).cacheOnDisc(true).build();
                                                        mImageLoader = ImageLoader.getInstance();
                                                        mImageLoader.displayImage(userImage, mNapperUser_profile_image, options);
                                                    }
                                                    else{
                                                        mNapperUser_profile_image.setBackgroundResource(R.drawable.user_icon);
                                                    }
                                                }

                                            }

                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }

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
}


package com.napzzz.Activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gcm.GCMRegistrar;
import com.napzzz.R;

public class LoginActivity extends Activity implements OnClickListener {

    Button facebookBtn, googleBtn, signupBtn, loginBtn;
    //For communicating with Google APIs
    //private GoogleApiClient mGoogleApiClient;
    private boolean mSignInClicked;
    private boolean mIntentInProgress;
    // contains all possible error codes for when a client fails to connect to
    // Google Play services
    //private ConnectionResult mConnectionResult;
    private static final int PICK_MEDIA_REQUEST_CODE = 8;
    private static final int SHARE_MEDIA_REQUEST_CODE = 9;
    private static final int SIGN_IN_REQUEST_CODE = 10;
    private static final int ERROR_DIALOG_REQUEST_CODE = 11;

    SharedPreferences login_pref;
    boolean hasLogin;
    String deviceID = "", registrationId = "", version = "", google_id, google_userName = null, google_birthday = null, google_userEmail = null, google_userProfilePicUrl;
    private ProgressDialog pDialog;
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    //GCM PROJECT ID
    static final String SENDER_ID = "822012508209";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        facebookBtn = (Button) findViewById(R.id.facebookBtn);
        googleBtn = (Button) findViewById(R.id.googleBtn);
        signupBtn = (Button) findViewById(R.id.signupBtn);
        loginBtn = (Button) findViewById(R.id.loginBtn);

        login_pref = getApplicationContext().getSharedPreferences("LoginPrefs", Activity.MODE_PRIVATE);
        String has_login = login_pref.getString("has_login", "");

        if (has_login.equals("1")) {
            //Go directly to main activity.
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(myIntent);
        } else {

            loginBtn.setOnClickListener(this);
            signupBtn.setOnClickListener(this);
            googleBtn.setOnClickListener(this);

        }
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.googleBtn:

                break;

            case R.id.loginBtn:
                Intent in = new Intent(LoginActivity.this, NapperProfileActivity.class);
                startActivity(in);
                finish();
               // Intent in = new Intent(LoginActivity.this, LoginDetailActivity.class);
               // startActivity(in);
                //	finish();
                break;

            case R.id.signupBtn:

                Intent intt = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intt);
                //finish();

                break;
        }
        return;
    }


}

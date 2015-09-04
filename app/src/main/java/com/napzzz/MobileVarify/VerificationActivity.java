package com.napzzz.MobileVarify;

import com.napzzz.R;
import com.sinch.verification.Config;
import com.sinch.verification.SinchVerification;
import com.sinch.verification.Verification;
import com.sinch.verification.VerificationException;
import com.sinch.verification.VerificationListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class VerificationActivity extends Activity {

    private static final String TAG = Verification.class.getSimpleName();
    private final String APPLICATION_KEY = "63aa7d75-7b08-4862-8648-16fd4dcabb6f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressIndicator);
        progressBar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        if (intent != null) {
            String phoneNumber = intent.getStringExtra(MobileVarificationActivity.INTENT_PHONENUMBER);
            String method = intent.getStringExtra(MobileVarificationActivity.INTENT_METHOD);
            TextView phoneText = (TextView) findViewById(R.id.numberText);
            phoneText.setText(phoneNumber);
            createVerification(phoneNumber, method);
        }

    }

    void createVerification(String phoneNumber, String method) {
        Config config = SinchVerification.config().applicationKey(APPLICATION_KEY).context(getApplicationContext())
                .build();
        VerificationListener listener = new MyVerificationListener();
        Verification verification;
        if (method.equalsIgnoreCase(MobileVarificationActivity.SMS)) {
            verification = SinchVerification.createSmsVerification(config, phoneNumber, listener);
        } else {
            TextView messageText = (TextView) findViewById(R.id.textView);
            messageText.setText(R.string.flashcalling);
            verification = SinchVerification.createFlashCallVerification(config, phoneNumber, listener);
        }
        verification.initiate();
    }

    class MyVerificationListener implements VerificationListener {

        @Override
        public void onInitiated() {
            Log.d(TAG, "Initialized!");
        }

        @Override
        public void onInitiationFailed(Exception exception) {
            Log.e(TAG, "Verification initialization failed: " + exception.getMessage());
            hideProgress(R.string.failed, false);
        }

        @Override
        public void onVerified() {
            Log.d(TAG, "Verified!");
            hideProgress(R.string.verified, true);
        }

        @Override
        public void onVerificationFailed(Exception exception) {
            Log.e(TAG, "Verification failed: " + exception.getMessage());
            hideProgress(R.string.failed, false);
        }
    }

    void hideProgress(int message, boolean success) {
        if (success) {
            ImageView checkMark = (ImageView) findViewById(R.id.checkmarkImage);
            checkMark.setVisibility(View.VISIBLE);
        }
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressIndicator);
        progressBar.setVisibility(View.INVISIBLE);
        TextView progressText = (TextView) findViewById(R.id.progressText);
        progressText.setVisibility(View.INVISIBLE);
        TextView messageText = (TextView) findViewById(R.id.textView);
        messageText.setText(message);
    }

}

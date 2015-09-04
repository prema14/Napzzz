package com.napzzz.MobileVarify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.napzzz.R;

/**
 * Created by koizen12 on 9/4/15.
 */
public class MobileVarificationActivity extends Activity {
    public static final String SMS = "sms";
    public static final String FLASHCALL = "flashcall";
    public static final String INTENT_PHONENUMBER = "phonenumber";
    public static final String INTENT_METHOD = "method";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.phone_vary_main);

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        TextView phoneNumber = (TextView) findViewById(R.id.phoneNumber);
        phoneNumber.setText(manager.getLine1Number());
    }

    private void openActivity(String phoneNumber, String method) {
        Intent verification = new Intent(this, VerificationActivity.class);
        verification.putExtra(INTENT_PHONENUMBER, phoneNumber);
        verification.putExtra(INTENT_METHOD, method);
        startActivity(verification);
    }

    private boolean checkInput() {
        TextView phoneNumber = (TextView) findViewById(R.id.phoneNumber);
        if (phoneNumber.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please input a phone number.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void onButtonClicked(View view) {
        if (checkInput()) {
            TextView phoneNumber = (TextView) findViewById(R.id.phoneNumber);
            if (view == findViewById(R.id.smsVerificationButton)) {
                openActivity(phoneNumber.getText().toString(), SMS);
            } else if (view == findViewById(R.id.callVerificationButton)) {
                openActivity(phoneNumber.getText().toString(), FLASHCALL);
            }
        }
    }

}

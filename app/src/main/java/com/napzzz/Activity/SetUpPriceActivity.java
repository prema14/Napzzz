package com.napzzz.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.napzzz.R;

/**
 * Created by koizen12 on 8/28/15.
 */
public class SetUpPriceActivity extends Activity {
    Button mbtn_back_setup_price, mbtn_next_setup_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_price);

        mbtn_back_setup_price = (Button) findViewById(R.id.btn_back_setup_price);
        mbtn_next_setup_price = (Button) findViewById(R.id.btn_next_setup_price);

        mbtn_next_setup_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent setupLocationIntent = new Intent(SetUpPriceActivity.this, SetupLocationFragnment.class);
                startActivity(setupLocationIntent);
                //go to SetupLocationFragnment page.
               /* FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SetupLocationFragnment mFragment = new SetupLocationFragnment();
                fragmentTransaction.replace(R.id.setup_price_fragment_container, mFragment, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
            }
        });



    }
}

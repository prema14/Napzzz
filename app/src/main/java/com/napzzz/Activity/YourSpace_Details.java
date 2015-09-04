package com.napzzz.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.napzzz.R;

public class YourSpace_Details extends Activity {
    private Button btn_next_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_space__details);
        btn_next_details=(Button)findViewById(R.id.btn_next_details);
        btn_next_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(YourSpace_Details.this, SetUpPriceActivity.class);
                startActivity(i);


            }
        });



    }


}
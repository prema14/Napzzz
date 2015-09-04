package com.napzzz.Activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.napzzz.R;


public class ListYourSpaceActivity extends Activity implements OnClickListener {

    Button hostingBtn, napperzBtn, btn_next;
    TextView txt_next;
    private Spinner typeSpinner, bedsSpinner, restroomSpinner, accomSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_your_space);

      /*hostingBtn=(Button) findViewById(R.id.hostingBtn);
      napperzBtn=(Button) findViewById(R.id.napperzBtn);*/
        btn_next = (Button) findViewById(R.id.btn_next);


        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        //roomSpinner=(Spinner) findViewById(R.id.roomSpinner);
        bedsSpinner = (Spinner) findViewById(R.id.bedsSpinner);
        restroomSpinner = (Spinner) findViewById(R.id.restroomSpinner);
        accomSpinner = (Spinner) findViewById(R.id.accomSpinner);

   /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      toolbar.setTitleTextColor(getResources().getColor(R.color.white));
      toolbar.setTitle("Napzzz");
      txt_next.setTypeface(null, Typeface.BOLD);
      setSupportActionBar(toolbar);*/



      /* hostingBtn.setOnClickListener(this);
      napperzBtn.setOnClickListener(this);*/
        btn_next.setOnClickListener(this);


        typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                String typeSpin = parent.getItemAtPosition(position).toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setTextSize(16);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });
/*
      roomSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
         public void onItemSelected(AdapterView<?> parent, View view,
               int position, long id) {
            // TODO Auto-generated method stub
            String roomSpin=parent.getItemAtPosition(position).toString();
            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
              ((TextView) parent.getChildAt(0)).setTextSize(16);

            }

         @Override
         public void onNothingSelected(AdapterView<?> adapter) {
            // TODO Auto-generated method stub

         }
      });*/

        bedsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                String bedsSpin = parent.getItemAtPosition(position).toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setTextSize(16);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        restroomSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                String restroomSpin = parent.getItemAtPosition(position).toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setTextSize(16);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        accomSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                String accomSpin = parent.getItemAtPosition(position).toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setTextSize(16);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        String zoneArr[] = new String[]{"Type", "Private Room", "Shared Room "};
        ArrayAdapter<String> zoneAdp = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_dropdown_layout, zoneArr);
        typeSpinner.setAdapter(zoneAdp);


        String bedsArr[] = new String[]{"Beds", "1", "2", "3", "4"};
        ArrayAdapter<String> bedsAdp = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_dropdown_layout, bedsArr);
        bedsSpinner.setAdapter(bedsAdp);

        String restroomArr[] = new String[]{"Restrooms", "Couch", "Hammock", "Airbed", "Other"};
        ArrayAdapter<String> restroomAdp = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_dropdown_layout, restroomArr);
        restroomSpinner.setAdapter(restroomAdp);

        String accomArr[] = new String[]{"Accommodate", "1", "2", "3", "4"};
        ArrayAdapter<String> accomAdp = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_dropdown_layout, accomArr);
        accomSpinner.setAdapter(accomAdp);


    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

      /*case R.id.hostingBtn:

         hostingBtn.setBackgroundColor(getResources().getColor(R.color.theme_color));
         napperzBtn.setBackgroundColor(getResources().getColor(R.color.dark_grey));

          break;

      case R.id.napperzBtn:

         hostingBtn.setBackgroundColor(getResources().getColor(R.color.dark_grey));
          napperzBtn.setBackgroundColor(getResources().getColor(R.color.theme_color));

          break;*/
            case R.id.btn_next:

                Intent i = new Intent(ListYourSpaceActivity.this, YourSpace_AddPhotos.class);
                startActivity(i);
                break;

        }
        return;
    }
}
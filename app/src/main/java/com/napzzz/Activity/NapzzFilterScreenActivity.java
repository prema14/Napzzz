package com.napzzz.Activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.napzzz.Api.AsyncApi;
import com.napzzz.R;
import com.napzzz.Utils.RangeSeekBar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by pramod on 9/01/15.
 */
public class NapzzFilterScreenActivity extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Spinner mFilterSpinner;
    SeekBar mFilterSeekBar;
    TextView mTextViewBathroom, mTextViewRooms, mTextViewBeds, mTextViewOccomodates, mTextViewMinPrice, mTextViewMaxPrice;
    ImageView mImageViewBathroomBtnLeft, mImageViewBathroomBtnRight, mImageViewRoomBtnLeft, mImageViewRoomBtnRight,
            mImageViewBedsBtnLeft, mImageViewBedsBtnRight, mImageViewOccomodatesBtnLeft, mImageViewOccomodatesBtnRight;
    Button mBtnApply;
    String mBathroom, mBeds, mRooms, mOcccomodations, mListingType;
    private String[] state = {"Private Room", "Arunachal Pradesh", "Assam", "Bihar", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Tamil Nadu"};
    int numberRoom = 0;
    int numberBathRoom = 0;
    int numberBed = 0;
    int numberOccomodates = 0;
    int minvalue;
    int maxvalue;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_screen);

        // view registration.
        mFilterSpinner = (Spinner) findViewById(R.id.filterSpinner);
        //mFilterSeekBar = (SeekBar) findViewById(R.id.filterSeekBar);

        mTextViewBathroom = (TextView) findViewById(R.id.etextbathroom);
        mTextViewRooms = (TextView) findViewById(R.id.etextroom);
        mTextViewBeds = (TextView) findViewById(R.id.etextbed);
        mTextViewOccomodates = (TextView) findViewById(R.id.etextoccomodates);

        mTextViewMinPrice = (TextView) findViewById(R.id.filter_minprice);
        mTextViewMaxPrice = (TextView) findViewById(R.id.filter_maxprice);

        mBtnApply = (Button) findViewById(R.id.btnnapzzfilter);
        //for increase or decrease btn.
        mImageViewBathroomBtnLeft = (ImageView) findViewById(R.id.imageViewFilterbathroomLft);
        mImageViewBathroomBtnRight = (ImageView) findViewById(R.id.imageViewFilterbathroomRht);
        mImageViewRoomBtnLeft = (ImageView) findViewById(R.id.imageViewFilterroomLft);
        mImageViewRoomBtnRight = (ImageView) findViewById(R.id.imageViewFilterroomRht);
        mImageViewBedsBtnLeft = (ImageView) findViewById(R.id.imageViewFilterBedLft);
        mImageViewBedsBtnRight = (ImageView) findViewById(R.id.imageViewFilterBedRht);
        mImageViewOccomodatesBtnLeft = (ImageView) findViewById(R.id.imageViewFilterOccomodatesLft);
        mImageViewOccomodatesBtnRight = (ImageView) findViewById(R.id.imageViewFilterOccomodatesRht);

        // create RangeSeekBar as Integer range between 20 and 75
        RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(0, 100, NapzzFilterScreenActivity.this);
        seekBar.setBackgroundColor(9);
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                minvalue = minValue;
                maxvalue = maxValue;
                mTextViewMinPrice.setText("$" + Integer.toString(minvalue));
                mTextViewMaxPrice.setText("$" + Integer.toString(maxvalue));
                // handle changed range values
                Log.i("NapzzFilterActivity.TAG", "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
            }
        });

// add RangeSeekBar to pre-defined layout
        ViewGroup layout = (ViewGroup) findViewById(R.id.ll_seekbar);
        layout.addView(seekBar);

        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, state);
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFilterSpinner.setAdapter(adapter_state);
        mFilterSpinner.setOnItemSelectedListener(this);

        mImageViewBathroomBtnLeft.setOnClickListener(this);
        mImageViewBathroomBtnRight.setOnClickListener(this);
        mImageViewRoomBtnLeft.setOnClickListener(this);
        mImageViewRoomBtnRight.setOnClickListener(this);
        mImageViewBedsBtnLeft.setOnClickListener(this);
        mImageViewBedsBtnRight.setOnClickListener(this);
        mImageViewOccomodatesBtnLeft.setOnClickListener(this);
        mImageViewOccomodatesBtnRight.setOnClickListener(this);

        mBtnApply.setOnClickListener(this);

        serverRequest();


    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mFilterSpinner.setSelection(position);
        mListingType = (String) mFilterSpinner.getSelectedItem();
        Toast.makeText(NapzzFilterScreenActivity.this, mListingType, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnnapzzfilter:

             /*   Intent napzzHomeScreenIntent = new Intent(NapzzFilterScreenActivity.this, NapzzHomeScreenActivity.class);
                napzzHomeScreenIntent.putExtra("listing_type" , mListingType);
                napzzHomeScreenIntent.putExtra("min_ratio_value" , minvalue);
                napzzHomeScreenIntent.putExtra("max_ratio_value" , maxvalue);
                napzzHomeScreenIntent.putExtra("bed_value", numberRoom);
                napzzHomeScreenIntent.putExtra("room_value", numberRoom);
                napzzHomeScreenIntent.putExtra("bathroom_value", numberBathRoom);
                napzzHomeScreenIntent.putExtra("occomodates_value", numberOccomodates);*/

                Toast.makeText(NapzzFilterScreenActivity.this, "listing_type " + ":" + mListingType + "min_ratio_value :" + ":" + minvalue +
                        "max_ratio_value :" + ":" + maxvalue + "bed_value :" + ":" + numberBed + "room_value :" + ":" + numberRoom +
                        "bathroom_value :" + ":" + numberBathRoom + "occomodates_value :" + ":" + numberOccomodates, Toast.LENGTH_SHORT).show();
                //  startActivity(napzzHomeScreenIntent);


                break;

            case R.id.imageViewFilterbathroomLft:

                try {
                    numberBathRoom = Integer.parseInt(mTextViewBathroom.getText().toString());

                    if (numberBathRoom > 0) {
                        numberBathRoom = numberBathRoom - 1;
                        mTextViewBathroom.setText(Integer.toString(numberBathRoom));
                    }


                } catch (NumberFormatException e) {

                    System.out.println("parse value is not valid : " + e);

                }

                break;

            case R.id.imageViewFilterbathroomRht:

                try {
                    numberBathRoom = Integer.parseInt(mTextViewBathroom.getText().toString());

                    if (numberBathRoom >= 0) {
                        numberBathRoom = numberBathRoom + 1;
                        mTextViewBathroom.setText(Integer.toString(numberBathRoom));
                    }


                } catch (NumberFormatException e) {

                    System.out.println("parse value is not valid : " + e);

                }
                break;

            case R.id.imageViewFilterroomLft:

                try {
                    numberRoom = Integer.parseInt(mTextViewRooms.getText().toString());

                    if (numberRoom > 0) {
                        numberRoom = numberRoom - 1;
                        mTextViewRooms.setText(Integer.toString(numberRoom));
                    }


                } catch (NumberFormatException e) {

                    System.out.println("parse value is not valid : " + e);

                }

                break;

            case R.id.imageViewFilterroomRht:
                try {
                    numberRoom = Integer.parseInt(mTextViewRooms.getText().toString());

                    if (numberRoom >= 0) {
                        numberRoom = numberRoom + 1;
                        mTextViewRooms.setText(Integer.toString(numberRoom));
                    }


                } catch (NumberFormatException e) {

                    System.out.println("parse value is not valid : " + e);

                }
                break;

            case R.id.imageViewFilterBedLft:

                try {
                    numberBed = Integer.parseInt(mTextViewBeds.getText().toString());

                    if (numberBed > 0) {
                        numberBed = numberBed - 1;
                        mTextViewBeds.setText(Integer.toString(numberBed));
                    }


                } catch (NumberFormatException e) {

                    System.out.println("parse value is not valid : " + e);

                }

                break;

            case R.id.imageViewFilterBedRht:
                try {
                    numberBed = Integer.parseInt(mTextViewBeds.getText().toString());

                    if (numberBed >= 0) {
                        numberBed = numberBed + 1;
                        mTextViewBeds.setText(Integer.toString(numberBed));
                    }


                } catch (NumberFormatException e) {

                    System.out.println("parse value is not valid : " + e);

                }
                break;

            case R.id.imageViewFilterOccomodatesLft:

                try {
                    numberOccomodates = Integer.parseInt(mTextViewOccomodates.getText().toString());

                    if (numberOccomodates > 0) {
                        numberOccomodates = numberOccomodates - 1;
                        mTextViewOccomodates.setText(Integer.toString(numberOccomodates));
                    }


                } catch (NumberFormatException e) {

                    System.out.println("parse value is not valid : " + e);

                }

                break;

            case R.id.imageViewFilterOccomodatesRht:
                try {
                    numberOccomodates = Integer.parseInt(mTextViewOccomodates.getText().toString());

                    if (numberOccomodates >= 0) {
                        numberOccomodates = numberOccomodates + 1;
                        mTextViewOccomodates.setText(Integer.toString(numberOccomodates));
                    }

                } catch (NumberFormatException e) {

                    System.out.println("parse value is not valid : " + e);

                }
                break;
        }
        return;
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
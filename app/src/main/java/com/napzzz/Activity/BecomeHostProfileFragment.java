package com.napzzz.Activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.napzzz.R;

/**
 * Created by pramod on 8/27/15.
 */
public class BecomeHostProfileFragment extends Fragment {
    ImageView leftHeaderIconProfileBooking;
    Button btnListYourSpace, btnManageListing, btnWhyHost;


    Toolbar mToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_become_host_profile, container, false);
        btnListYourSpace = (Button) rootView.findViewById(R.id.btn_list_ur_space);
        btnManageListing = (Button) rootView.findViewById(R.id.btn_mng_listing);
        btnWhyHost = (Button) rootView.findViewById(R.id.btn_why_host);

        btnListYourSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mngListingIntent = new Intent(getActivity(), ListYourSpaceActivity.class);
                startActivity(mngListingIntent);

            }
        });



        return rootView;

    }
}


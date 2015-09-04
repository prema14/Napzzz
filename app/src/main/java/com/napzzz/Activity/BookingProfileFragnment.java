package com.napzzz.Activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.napzzz.R;

/**
 * Created by pramod on 8/27/15.
 */
public class BookingProfileFragnment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_booking_profile, container, false);
        return rootView;

    }
}

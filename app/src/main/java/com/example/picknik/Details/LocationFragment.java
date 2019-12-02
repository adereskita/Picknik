package com.example.picknik.Details;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.picknik.MapsActivity;
import com.example.picknik.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class LocationFragment extends Fragment {
    private static final String TAG = "LocationFragment";

    private static final int ERROR_DIALOG_REQUEST = 9001;
    public static final String EXTRA_LAT = "lat";
    public static final String EXTRA_LNG = "lng";

    private double iLat,iLng;

    private Button btnMap;


    public LocationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.location_fragment,container, false);


//        if (isServiceOK()){
            btnMap = view.findViewById(R.id.btn_map);

            btnMap.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                    intent.putExtra(EXTRA_LAT, iLat);
                    intent.putExtra(EXTRA_LNG, iLng);
                    startActivity(intent);
                }
            });
//        }


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Intent I = getActivity().getIntent();
        iLat = I.getDoubleExtra(EXTRA_LAT, iLat);
        iLng = I.getDoubleExtra(EXTRA_LNG, iLng);


    }



    public boolean isServiceOK(){
        Log.d(TAG, "isServiceOK: Checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());

        if (available == ConnectionResult.SUCCESS){
            //user can make map requests
            Log.d(TAG, "isServiceOK: google play services is working");
            return true;
        }else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServiceOK: and error Occured");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else {
            Toast.makeText(getContext(),"You can't make map Requests",Toast.LENGTH_SHORT).show();
        }
        return false;
    }


}

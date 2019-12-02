package com.example.picknik.Profile;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.picknik.Adaptor.RecyclerAdapterProfile;
import com.example.picknik.R;
import com.example.picknik.Utils.PostModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PlaceFragment extends Fragment {

    private static final String TAG = "DaylineFragment";

    //var
    private String userId, postId;
    private RecyclerView mProfileRecyclerView;
    private ArrayList<PostModel> listData;
    RecyclerAdapterProfile adapter;
    private AdapterView.OnItemSelectedListener listener;

    //error
    private TextView errorText;
    private ImageView errorImage;


    //db
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef, mDbPhotos;
    private StorageReference mStorageRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.place_fragment,container,false);


        errorText = view.findViewById(R.id.err_msg);
        errorImage = view.findViewById(R.id.err_picture);


        try {
            mProfileRecyclerView = view.findViewById(R.id.dayline_recycleview);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mProfileRecyclerView.setLayoutManager(layoutManager);

            RecyclerAdapterProfile adapter;

            //make vertical adapter
            adapter = new RecyclerAdapterProfile(getContext(),listData);
            mProfileRecyclerView.setAdapter(adapter);
            mProfileRecyclerView.setHasFixedSize(true);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mProfileRecyclerView.getContext(),
                    layoutManager.getOrientation());
            mProfileRecyclerView.addItemDecoration(dividerItemDecoration);



            if (!isConnected(getContext())){
                errorText.setVisibility(View.VISIBLE);
                errorImage.setVisibility(View.VISIBLE);
                mProfileRecyclerView.setVisibility(View.GONE);
            }else if (isConnected(getContext())){
                errorText.setVisibility(View.GONE);
                errorImage.setVisibility(View.GONE);
                mProfileRecyclerView.setVisibility(View.VISIBLE);
            }

        }catch (Exception e){
            e.getMessage();
        }


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //auth user
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        userId = user.getUid();
        mDbRef = mDatabase.getReference().child("user").child("picknik-post");

        mStorageRef = FirebaseStorage.getInstance().getReference();


        Query query = mDbRef.orderByChild("id_user").equalTo(userId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }// end of on create


    public boolean isConnected(Context context){

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null
                    && wifi.isConnectedOrConnecting())) return true;

        else return false;

        } else

        return false;

    }




    @Override
    public void onStart() {
        super.onStart();
//
//        mDbRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                showData(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getContext(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void showData(DataSnapshot dataSnapshot){

        if (dataSnapshot.exists()) {

            listData = new ArrayList<>();
            listData.clear();
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                // read the index key

                PostModel mUser=ds.getValue(PostModel.class);

                if (mUser.getId_post() != null && mUser.getType().equals("Place")){
                    listData.add(mUser);
                }else{
                    listData.remove(mUser);
                }

                //display all the information
            }
            adapter=new RecyclerAdapterProfile(getContext(),listData);
            mProfileRecyclerView.setAdapter(adapter);

        }

    }



}

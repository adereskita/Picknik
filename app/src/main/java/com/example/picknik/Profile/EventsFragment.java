package com.example.picknik.Profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.picknik.Adaptor.RecyclerAdapterProfile;
import com.example.picknik.R;
import com.example.picknik.Utils.PostModel;
import com.example.picknik.Utils.User;
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

public class EventsFragment extends Fragment {

    //var
    private String userId, postId;
    private RecyclerView mProfileRecyclerView;
    private ArrayList<PostModel> listData;
    RecyclerAdapterProfile adapter;
    private AdapterView.OnItemSelectedListener listener;


    //db
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef, mDbPhotos;
    private StorageReference mStorageRef;

    public EventsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photos_fragment,container,false);

        try {
            mProfileRecyclerView = view.findViewById(R.id.dayline_recycleview);


            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mProfileRecyclerView.setLayoutManager(layoutManager);
            //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            // verticalRecyclerView.setLayoutManager(layoutManager);
            RecyclerAdapterProfile adapter;

            //make vertical adapter
            adapter = new RecyclerAdapterProfile(getContext(),listData);
            mProfileRecyclerView.setAdapter(adapter);

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
        if (!user.isAnonymous()){
            userId = user.getUid();
        }else {
            userId = null;
        }
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

        Query qEvent = mDbRef.orderByChild("type").equalTo("Event");
        qEvent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listData = new ArrayList<>();
                    listData.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        // read the index key

                        PostModel mUser=ds.getValue(PostModel.class);

//                user.setImageSrc(ds.child(userId).child("picknik-post").child("photos").child(user.getId_post()).getValue(User.class).getImageSrc());

//                pModel.setNamatempat(ds.child(userId).child("picknik-post").child(postId).getValue(PostModel.class).getNamatempat()); //set the tempat
//                pModel.setCategory(ds.child(userId).child("picknik-post").child(postId).getValue(PostModel.class).getCategory()); //set the category
//                pModel.setLocation(ds.child(userId).child("picknik-post").child(postId).getValue(PostModel.class).getLocation()); //set the alamat


                        if (mUser.getId_post() != null && mUser.getType().equals("Event")){
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Opsss.. Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

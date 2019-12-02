package com.example.picknik.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.picknik.Adaptor.RecycleViewAdapter;
import com.example.picknik.Adaptor.RecyclerAdapterProfile;
import com.example.picknik.Adaptor.RecyclerReview;
import com.example.picknik.R;
import com.example.picknik.Review.ReviewActivity;
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

import java.util.ArrayList;

import static com.example.picknik.Details.DetailActivity.EXTRA_IDPOST;

public class ReviewProfileFragment extends Fragment {

    private RecyclerView mReviewRecyclerView;
    private RecyclerReview adapter;
    private ArrayList<PostModel> listData;
    private String id_user,id_post;

    //db
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profilereview_fragment,container,false);

        try {

            mReviewRecyclerView = view.findViewById(R.id.pReview_recyclerview);
            mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

//            adapter = new RecyclerReview(listData, getContext());
//            mReviewRecyclerView.setAdapter(adapter);

            mReviewRecyclerView.setHasFixedSize(true);

        }catch (Exception e){
            e.getMessage();
        }

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent I = getActivity().getIntent();
        id_post = I.getStringExtra(EXTRA_IDPOST);

        //auth user
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        id_user = user.getUid();

        mDbRef = mDatabase.getReference().child("user").child("picknik-user-review")
        .child(id_user);

        Query query = mDbRef.orderByChild("id_user").equalTo(id_user);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setReview(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getContext(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setReview(DataSnapshot dataSnapshot){

        if (dataSnapshot.exists()) {

            listData = new ArrayList<>();
            listData.clear();
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                // read the index key

                PostModel mUser=ds.getValue(PostModel.class);

                System.out.println(mUser.getTxt_rating());

                if (mUser.getId_rating() != null){
                    listData.add(mUser);
                }else{
                    listData.remove(mUser);
                }

                //display all the information
            }
            adapter=new RecyclerReview(listData, getContext());
            mReviewRecyclerView.setAdapter(adapter);



        }
    }
}

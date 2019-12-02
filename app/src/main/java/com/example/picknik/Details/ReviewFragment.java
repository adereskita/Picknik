package com.example.picknik.Details;

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
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.picknik.Adaptor.RecyclerReview;
import com.example.picknik.R;
import com.example.picknik.Review.AddReview;
import com.example.picknik.Review.ReviewActivity;
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

import java.sql.SQLOutput;
import java.util.ArrayList;

public class ReviewFragment extends Fragment {
    private static final String TAG = "ForyouFragment";

    public static final String EXTRA_IDPOST = "id_post";
    private String id_post;

    private RecyclerView mReviewRecyclerView;
    private RecyclerReview adapter;
    private ArrayList<PostModel> listData;
    private Button btn_addReview, btn_seeAllrev;
    private RatingBar ratingBar;

    private TextView tv_review_all;


    //db
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;


    public ReviewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review,container,false);

        btn_addReview = view.findViewById(R.id.btn_writeReview);
        btn_addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                if (!mUser.isAnonymous()) {
                    Intent intent = new Intent(getContext(), AddReview.class);
                    //put whatever data you need to send
                    intent.putExtra(EXTRA_IDPOST, id_post);
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "You Must Login First.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_seeAllrev = view.findViewById(R.id.btn_seeAll);
        btn_seeAllrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReviewActivity.class);
                //put whatever data you need to send
                intent.putExtra(EXTRA_IDPOST, id_post);
                startActivity(intent);
            }
        });

        ratingBar = view.findViewById(R.id.rb_rating_frag);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                Toast.makeText(getContext(), "Rating"+rating, Toast.LENGTH_SHORT).show();
            }
        });


        tv_review_all = view.findViewById(R.id.tv_rating);

        try {
            mReviewRecyclerView = view.findViewById(R.id.recycler_review);

            mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            adapter = new RecyclerReview(listData, getContext());
            mReviewRecyclerView.setAdapter(adapter);

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

        mDbRef = mDatabase.getReference().child("user").child("picknik-review")
        .child(id_post);

        listData = new ArrayList<>();
//        Query query = mDbRef.orderByChild("id_post").equalTo(id_post);
        mDbRef.limitToFirst(6).addValueEventListener(new ValueEventListener() {
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
                PostModel mRate = new PostModel();

                PostModel mPost=ds.getValue(PostModel.class);

                if (mPost.getId_rating() !=null){
                    listData.add(mPost);
                }else {
                    listData.clear();
                }
//                mRate.setRating(ds.getValue(PostModel.class).getRating());

               // tv_review_all.setText(String.valueOf(mRate.getRating()));

                //display all the information
            }
            adapter=new RecyclerReview(listData, getContext());
            mReviewRecyclerView.setAdapter(adapter);

        }

        //        listData.add(new PostModel("364353","Picknik Official","Keren","MAKANANNYA ENAK",2));
    }

}

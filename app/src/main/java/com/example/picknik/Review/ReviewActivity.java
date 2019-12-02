package com.example.picknik.Review;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.picknik.Adaptor.RecyclerReview;
import com.example.picknik.R;
import com.example.picknik.Utils.PostModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.picknik.Details.DetailActivity.EXTRA_IDPOST;

public class ReviewActivity extends AppCompatActivity {

    private RatingBar addRating;
    private RecyclerView mReviewRecyclerView;
    private RecyclerReview adapter;
    private ArrayList<PostModel> listData;
    private Button btn_addReview, btn_seeAllrev;
    private RatingBar ratingBar;
    private String id_post;


    //db
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);


        Intent I = getIntent();
        id_post = I.getStringExtra(EXTRA_IDPOST);

        //auth user
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        mDbRef = mDatabase.getReference().child("user").child("picknik-review")
                .child(id_post);

        addRating = findViewById(R.id.rb_rating_seeAll);
        addRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(ReviewActivity.this, "Rating"+rating, Toast.LENGTH_SHORT).show();
            }
        });

        btn_addReview = findViewById(R.id.btn_writeReviews);
        btn_addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                if (!mUser.isAnonymous()) {
                    Intent intent = new Intent(getApplicationContext(), AddReview.class);
                    //put whatever data you need to send
                    intent.putExtra(EXTRA_IDPOST, id_post);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "You Must Login First.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        try {
            mReviewRecyclerView = findViewById(R.id.recycler_review);

            mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

            mReviewRecyclerView.setHasFixedSize(true);

        }catch (Exception e){
            e.getMessage();
        }



//        Query query = mDbRef.orderByChild("id_post").equalTo(id_post);
        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setReview(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
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
            adapter=new RecyclerReview(listData, getApplicationContext());
            mReviewRecyclerView.setAdapter(adapter);

        }

        //        listData.add(new PostModel("364353","Picknik Official","Keren","MAKANANNYA ENAK",2));
    }

}

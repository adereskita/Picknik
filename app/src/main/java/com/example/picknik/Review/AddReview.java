package com.example.picknik.Review;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.picknik.Details.DetailActivity;
import com.example.picknik.Profile.ProfileActivity;
import com.example.picknik.R;
import com.example.picknik.Utils.PostModel;
import com.example.picknik.Utils.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddReview extends AppCompatActivity {

    public static final String EXTRA_IDPOST = "id_post";

    float rate;
    private String id_post, reviewId,user_id,username;

    private ImageButton btn_back;
    private RatingBar addRating;

    //db
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private DatabaseReference dbReview, dbUser, dbUserReview;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        Intent I = getIntent();
        id_post = I.getStringExtra(EXTRA_IDPOST);



        mUser = FirebaseAuth.getInstance().getCurrentUser();
        user_id = mUser.getUid();

        dbReview = FirebaseDatabase.getInstance().getReference("user")
                .child("picknik-review");
        dbUser = FirebaseDatabase.getInstance().getReference();

        dbUserReview = FirebaseDatabase.getInstance().getReference("user")
                .child("picknik-user-review");


        edt_review = findViewById(R.id.edt_reviewAdd);
        edt_title_review = findViewById(R.id.edt_title_review);


        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                startActivity(intent);

            }
        });

        addRating = findViewById(R.id.rb_rating_add);
        addRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(AddReview.this, "Rating"+rating, Toast.LENGTH_SHORT).show();
                rate = rating;
            }
        });

        Button add_review = findViewById(R.id.btn_addReview);
        add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReviewAdd();
            }
        });


        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        User nUser = new User();

                        nUser.setUsername(ds.child(user_id).getValue(User.class).getUsername());

                        username = nUser.getUsername();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private EditText edt_title_review, edt_review;

    private void ReviewAdd(){

        final float rateNum = rate;
        final String title_review = edt_title_review.getText().toString().trim();
        final String txt_review = edt_review.getText().toString().trim();

        if (!TextUtils.isEmpty(title_review) && !TextUtils.isEmpty(txt_review) && rateNum != 0){
            if (mUser != null && !mUser.isAnonymous()){


                reviewId = dbReview.push().getKey();

                PostModel mPost = new PostModel(reviewId, user_id,username,title_review, txt_review, rateNum);

                dbReview.child(id_post).child(reviewId).setValue(mPost);

                dbUserReview.child(user_id).child(id_post).setValue(mPost);


                finish();

                Toast.makeText(getApplicationContext(), "Review Submitted",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), DetailActivity.class));

            }else {
                Toast.makeText(AddReview.this, "Please, Login before you proceed",
                        Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(AddReview.this, "Please, Complete the data.",
                    Toast.LENGTH_SHORT).show();
        }

    }
}

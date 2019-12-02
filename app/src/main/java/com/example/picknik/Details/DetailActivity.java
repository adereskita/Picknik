package com.example.picknik.Details;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.picknik.Adaptor.ImageSlideAdaptor;
import com.example.picknik.Models.ImageData;
import com.example.picknik.Fragment.SectionPageAdapter;
import com.example.picknik.MainActivity;
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
import com.google.firebase.storage.StorageReference;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";


    public static final String EXTRA_PHONE = "phone";
    public static final String EXTRA_IDPOST = "id_post";
    public static final String EXTRA_IDUSER = "id_user";
    public static final String EXTRA_CATEGORY = "category";

    public static final String EXTRA_LAT = "lat";
    public static final String EXTRA_LNG = "lng";


    private LinearLayout sliderdots;
    private ArrayList<PostModel> listData;
    private ViewPager viewPager;
    private int dotscount;
    private ImageView[] dots;
    private String userId,postId,pictId;
    private String iPost, iUser,iPhone;

    private ImageSlideAdaptor imageSlideAdaptor;
    private SectionPageAdapter mSectionPageAdapter;
    private ViewPager mviewPager;

    //db
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef, mDbPhotos;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //for image carousel
        viewPager = findViewById(R.id.image_slide);

        sliderdots = findViewById(R.id.slider_dots);

        Intent I = getIntent();
        iPost = I.getStringExtra(EXTRA_IDPOST);
        iUser = I.getStringExtra(EXTRA_IDUSER);
        iPhone = I.getStringExtra(EXTRA_PHONE);

        //iPost = (PostModel) getIntent().getSerializableExtra("id_post");
        //auth user
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        userId = user.getUid();
        mDbRef = mDatabase.getReference().child("user").child("picknik-post");
        mDbPhotos = mDatabase.getReference().child("user")
                .child("picknik-post").child("photos").child(iPost);


        //fragment
        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mviewPager = findViewById(R.id.container_details);
        setupViewPager(mviewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mviewPager);


        ImageButton backButton = findViewById(R.id.btn_back);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        mDbPhotos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setSlider(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException(); // don't ignore errors
            }
        });


        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 4000, 6000);


    }// end of onCreate



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id =item.getItemId();

        if (id == android.R.id.home){
            this.finish();
        }
                return super.onOptionsItemSelected(item);
    }


    public class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            DetailActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int i= 0;
                        if (viewPager.getCurrentItem() == i){
                            i++;
                            viewPager.setCurrentItem(i);
                        }else if (viewPager.getCurrentItem() == i){
                            i++;
                            viewPager.setCurrentItem(i);
                        }else {

                            viewPager.setCurrentItem(0);
                        }
                }
            });
        }
    }



    public void setSlider(DataSnapshot dataSnapshot){

        listData = new ArrayList<>();
            for (DataSnapshot ds: dataSnapshot.getChildren()) {


                PostModel mPost = ds.getValue(PostModel.class);

                pictId = ds.getKey();

                listData.add(mPost);

            }

            imageSlideAdaptor = new ImageSlideAdaptor(getApplicationContext(), listData);
            viewPager.setAdapter(imageSlideAdaptor);

            dotscount = imageSlideAdaptor.getCount();
            dots = new ImageView[dotscount];

            for (int i = 0;i < dotscount;i++){

                dots[i] = new ImageView(getApplicationContext());
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                params.setMargins(8, 0, 8, 0);
                sliderdots.addView(dots[i],params);

            }// end of for

            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        }


    private void setupViewPager(ViewPager viewPager){
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new DetailsFragment(), "Details");
        adapter.addFragment(new LocationFragment(),"Location");
        adapter.addFragment(new ReviewFragment(), "Review");
        viewPager.setAdapter(adapter);
    }




//CALLING INTENT
    public void onDialButton(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ iPhone.toString()));
                startActivity(intent);


    }
}

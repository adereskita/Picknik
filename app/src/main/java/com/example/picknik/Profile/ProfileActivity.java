package com.example.picknik.Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.picknik.Fragment.SectionPageAdapter;
import com.example.picknik.Login.MainActivity_Login;
import com.example.picknik.MainActivity;
import com.example.picknik.R;
import com.example.picknik.Search.SearchActivity;
import com.example.picknik.Utils.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    //vars
    private Button btn_addData, btn_editProf;
    private String UserId;
    private CircleImageView avatar;
    private TextView username,location,description;

    //fragment
    private ViewPager mViewPager;
    private SectionPageAdapter mSectionPageAdapter;

    //db
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;
    private StorageReference mStorageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //findView
        avatar = findViewById(R.id.avtr_profile);
        username= findViewById(R.id.user_name);
        location = findViewById(R.id.tv_origin);
        description = findViewById(R.id.tv_description);


        //auth user
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDbRef = mDatabase.getReference();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        UserId = user.getUid();


        mStorageRef = FirebaseStorage.getInstance().getReference();


        //get current user
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        mAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser cUser = firebaseAuth.getCurrentUser();

                if (cUser == null){
                    startActivity(new Intent(ProfileActivity.this, MainActivity_Login.class));
                    finish();
                }
            }
        };




        //button for Add
        btn_addData = findViewById(R.id.btn_inputPlace);
        btn_addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentUser.isAnonymous()){
                    Toast.makeText(getApplicationContext(),"Please, Login before you proceed",Toast.LENGTH_SHORT).show();
                }else

                startActivity(new Intent(ProfileActivity.this, AddPlaceActivity.class));
            }
        });

        //button edit profile
        btn_editProf = findViewById(R.id.edit_profile);
        btn_editProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentUser.isAnonymous()){
                    Toast.makeText(getApplicationContext(),"Please, Login before you proceed",Toast.LENGTH_SHORT).show();
                }else

                    startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });

        //toolbar
        Toolbar mToolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(mToolbar);


        //fragment
        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container_profile);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavMenu);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                        finish();
                        break;
                    case R.id.nav_search:
                        startActivity(new Intent(ProfileActivity.this, SearchActivity.class));
                        finish();
                        break;

                    case R.id.nav_profile:
                        getApplicationContext();
                        break;

                }

                return false;
            }
        });

    }//end of oncreate



//show data from Real-time Database
    private void showData(DataSnapshot dataSnapshot) {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (UserId != null && !mUser.isAnonymous()){
                User user = new User();
                user.setUser_id(ds.child(UserId).getValue(User.class).getUser_id());//Id User
                user.setUsername(ds.child(UserId).getValue(User.class).getUsername()); //set the name
                user.setAvatar(ds.child(UserId).getValue(User.class).getAvatar()); //set the avatar
                user.setLocation(ds.child(UserId).getValue(User.class).getLocation()); //set the email
                user.setDescription(ds.child(UserId).getValue(User.class).getDescription()); //set the description

                //display all the information
                username.setText(user.getUsername());

                location.setText(user.getLocation());
                if (location.getText().equals("")){
                    location.setVisibility(View.INVISIBLE);
                }
                description.setText(user.getDescription());
                if (description.getText().equals("")){
                    description.setText("No description of your Account.");
                }


                mStorageRef.child("Images").child(user.getAvatar()).getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(getApplicationContext())
                                        .load(uri)
                                        .into(avatar);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }else if (mFirebaseAuth.getCurrentUser().isAnonymous()){
                Toast.makeText(getApplicationContext(),"Please, Login before you proceed",Toast.LENGTH_SHORT).show();
            }else
                startActivity(new Intent(ProfileActivity.this, AddPlaceActivity.class));
        }

    }




    private void setupViewPager(ViewPager viewPager){
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
//Add Fragment
        adapter.addFragment(new PlaceFragment(),"Place");
        adapter.addFragment(new EventsFragment(),"Event");
        adapter.addFragment(new ReviewProfileFragment(),"Review");
        viewPager.setAdapter(adapter);
    }



    //under here for Menu function
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        // Inflate the menu options from the res/menu/menu_setting.xml file.
        // This adds menu items to the app bar.

        // Do authentication if user is anonymous
        if (mFirebaseAuth.getCurrentUser().isAnonymous()){
            getMenuInflater().inflate(R.menu.menu_in, menu);

        }else{
            getMenuInflater().inflate(R.menu.menu_out, menu);
        }
        return true;
//            getMenuInflater().inflate(R.menu.menu_setting, menu);
//            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);


        // Do authentication if user is anonymous
        switch (item.getItemId()) {
            // Respond to a click on the "Logout" menu option
            case R.id.action_logout:
                signOut();
                finish();
                return true;

            case R.id.action_login:
                    startActivity(new Intent(ProfileActivity.this, MainActivity_Login.class));
                    finish();
                    return true;

        }
        return super.onOptionsItemSelected(item);
    }



    //under here for Authentication User
    //sign out method
    private void signOut() {
        mFirebaseAuth.signOut();
    }


    // this listener will be called when there is change in firebase user session
    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(ProfileActivity.this, MainActivity_Login.class));
                finish();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(authListener);
        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            mFirebaseAuth.removeAuthStateListener(authListener);
        }
    }
}

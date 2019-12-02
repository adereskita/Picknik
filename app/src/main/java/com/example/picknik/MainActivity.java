package com.example.picknik;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.picknik.Login.MainActivity_Login;
import com.example.picknik.Fragment.CategoryFragment;
import com.example.picknik.Fragment.CollectionFragment;
import com.example.picknik.Fragment.ForyouFragment;
import com.example.picknik.Fragment.SectionPageAdapter;
import com.example.picknik.Profile.ProfileActivity;
import com.example.picknik.Search.SearchActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity_Login";

    //var
    //tabs fragment
    private SectionPageAdapter mSectionPageAdapter;
    private ViewPager mviewPager;

    //db
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        //auth user
        mFirebaseAuth = FirebaseAuth.getInstance();

        if (mFirebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), MainActivity_Login.class));
            finish();
        }


        //get current user
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        mAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser cUser = firebaseAuth.getCurrentUser();

                if (cUser == null){
                    startActivity(new Intent(MainActivity.this, MainActivity_Login.class));
                    finish();
                }
            }
        };


        //fragment
        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mviewPager = findViewById(R.id.container);
        setupViewPager(mviewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mviewPager);


        //bottom navigation intent
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavMenu);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        getApplicationContext();
                        return true;

                    case R.id.nav_search:
                        startActivity(new Intent(MainActivity.this, SearchActivity.class));
                        finish();
                        return true;

                    case R.id.nav_profile:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });

    }//end of onCreate

    private void setupViewPager(ViewPager viewPager){
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new ForyouFragment(), "For You");
        adapter.addFragment(new CollectionFragment(),"Collection");
        adapter.addFragment(new CategoryFragment(), "Category");
        viewPager.setAdapter(adapter);
    }


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
                startActivity(new Intent(MainActivity.this, MainActivity_Login.class));
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            mFirebaseAuth.removeAuthStateListener(authListener);
        }
    }

}

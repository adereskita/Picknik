package com.example.picknik.Search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.picknik.Adaptor.RecyclerAdapterProfile;
import com.example.picknik.Adaptor.SearchListAdapter;
import com.example.picknik.MainActivity;
import com.example.picknik.Profile.ProfileActivity;
import com.example.picknik.R;
import com.example.picknik.Utils.PostModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ArrayList<PostModel> listData;
    private RecyclerView recyclerView, mResultList;
    RecyclerAdapterProfile adapter;
    private Button btn_cancel;
    private SearchView mSearchView;

    private DatabaseReference dbUser;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //User firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        dbUser = FirebaseDatabase.getInstance().getReference("user")
                .child("picknik-post");


        mSearchView = findViewById(R.id.searchView);
        btn_cancel = findViewById(R.id.btn_cancel);

        //SEARCHING RESULT
        mResultList = findViewById(R.id.recycler_search);
        //LIST VIEW BEFORE SEARCHING
        recyclerView = findViewById(R.id.recycler_list_search);



        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.onActionViewCollapsed();
                btn_cancel.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                mResultList.setVisibility(View.GONE);
            }
        });

        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.onActionViewExpanded();
                btn_cancel.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                mResultList.setVisibility(View.VISIBLE);
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FirebaseDbSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //filter as you type
                FirebaseDbSearch(newText);
                return false;
            }
        });

        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.onActionViewExpanded();
                btn_cancel.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                mResultList.setVisibility(View.VISIBLE);
            }
        });

        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Opsss.. Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavMenu);
        bottomNavigationView.setSelectedItemId(R.id.nav_search);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(SearchActivity.this, MainActivity.class));
                        finish();
                        break;
                    case R.id.nav_search:
                        //startActivity(new Intent(SearchActivity.this, SearchActivity.class));
                        getApplicationContext();
                        break;

                    case R.id.nav_profile:
                        startActivity(new Intent(SearchActivity.this, ProfileActivity.class));
                        finish();
                        break;

                }

                return false;
            }
        });

    }


    private void FirebaseDbSearch(String searchText){

        Query mSearchQuery = dbUser.orderByChild("picknikName")
                .startAt(searchText).endAt(searchText + "\uf8ff");

        mSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listData = new ArrayList<>();
                if (dataSnapshot.exists()){

                    listData.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()){


                        PostModel mModel=ds.getValue(PostModel.class);

                        if (mModel.getId_post() != null){
                            listData.add(mModel);
                        }else{
                            listData.remove(mModel);
                        }
                    }

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    mResultList.setLayoutManager(layoutManager);
                    adapter=new RecyclerAdapterProfile(getApplicationContext(),listData);
                    mResultList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
//                    DividerItemDecoration divider = new DividerItemDecoration(SearchActivity.this,layoutManager.getOrientation());
//                    mResultList.addItemDecoration(divider);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Opsss.... Something is wrong",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setData(DataSnapshot datasnapshot){

        if (datasnapshot.exists()){
            listData = new ArrayList<>();
            listData.clear();
            for (DataSnapshot ds : datasnapshot.getChildren()){

                PostModel mModel=ds.getValue(PostModel.class);

                if (mModel.getId_post() != null && mModel.getType().equals("Place")){
                    listData.add(mModel);
                }else{
                    listData.remove(mModel);
                }
            }

            LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayout.VERTICAL,false);
            recyclerView.setLayoutManager(layoutManager);

            SearchListAdapter sAdapter;
            sAdapter = new SearchListAdapter(this, listData);
            //mResultList.setVisibility(GONE);
            recyclerView.setAdapter(sAdapter);

        }
    }
}

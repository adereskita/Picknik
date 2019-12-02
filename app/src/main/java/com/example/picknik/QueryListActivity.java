package com.example.picknik;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.picknik.Adaptor.RecyclerAdapterProfile;
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

import static com.example.picknik.Details.DetailActivity.EXTRA_CATEGORY;
import static com.example.picknik.Details.DetailActivity.EXTRA_IDPOST;
import static com.example.picknik.Details.DetailActivity.EXTRA_IDUSER;

public class QueryListActivity extends AppCompatActivity {

    //var
    private String userId, postId, category;
    private RecyclerView mQueryRecycler;
    private ArrayList<PostModel> listData;
    private RecyclerAdapterProfile adapter;
    private AdapterView.OnItemSelectedListener listener;
    private String iPost, iUser, iCategory;

    private TextView tv_category;

    //error
    private TextView errorText;
    private ImageView errorImage;


    //db
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef, mDbPhotos;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_list);


        //getting the data from another intent
        Intent I = getIntent();
        iPost = I.getStringExtra(EXTRA_IDPOST);
        iUser = I.getStringExtra(EXTRA_IDUSER);
        iCategory = I.getStringExtra(EXTRA_CATEGORY);


        System.out.println(iCategory + " category");


        errorText = findViewById(R.id.err_msg);
        errorImage = findViewById(R.id.err_picture);
        mQueryRecycler = findViewById(R.id.recycler_query);
        tv_category = findViewById(R.id.tv_category);

        //auth user
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        userId = user.getUid();
        mDbRef = mDatabase.getReference().child("user").child("picknik-post");

        mStorageRef = FirebaseStorage.getInstance().getReference();


        tv_category.setText(iCategory);

        if (iCategory.equals("Place")){
            Query query = mDbRef.orderByChild("type").equalTo(iCategory);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    showData(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Query query = mDbRef.orderByChild("category").equalTo(iCategory);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    showData(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void showData(DataSnapshot dataSnapshot){

        if (dataSnapshot.exists()) {

            listData = new ArrayList<>();
            listData.clear();
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                // read the index key

                PostModel mUser=ds.getValue(PostModel.class);

                if (mUser.getId_post() != null){
                    listData.add(mUser);
                }else{
                    listData.remove(mUser);
                }

                //display all the information
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                    LinearLayoutManager.VERTICAL, false);
            mQueryRecycler.setLayoutManager(layoutManager);

            adapter=new RecyclerAdapterProfile(getApplicationContext(),listData);
            mQueryRecycler.setAdapter(adapter);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                    mQueryRecycler.getContext(), layoutManager.getOrientation());
            mQueryRecycler.addItemDecoration(dividerItemDecoration);

        }

    }


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
    protected void onStart() {
        super.onStart();

        if (!isConnected(getApplicationContext())){
            errorText.setVisibility(View.VISIBLE);
            errorImage.setVisibility(View.VISIBLE);
            mQueryRecycler.setVisibility(View.GONE);
        }else if (isConnected(getApplicationContext())){
            errorText.setVisibility(View.GONE);
            errorImage.setVisibility(View.GONE);
            mQueryRecycler.setVisibility(View.VISIBLE);
        }
    }
}

package com.example.picknik.Details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.picknik.Models.ImageData;
import com.example.picknik.Adaptor.PreviewAdaptor;
import com.example.picknik.R;
import com.example.picknik.Utils.PostModel;
import com.example.picknik.Utils.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsFragment extends Fragment {

    public static final String EXTRA_PHONE = "phone";
    public static final String EXTRA_CATEGORY = "category";
    private static final String TAG = "DetailsFragment";

    public static final String EXTRA_IDPOST = "id_post";
    public static final String EXTRA_IDUSER = "id_user";

    private ArrayList<ImageData> imageData;
    private ArrayList<PostModel> detailData;
    private RecyclerView previewRecyclerView;
    private TextView picknikName, username,alamat,genre,category,time,avgCost,description,
            time_opens, time_closes;
    private CircleImageView avatar;
    private String userId, postId, iPost, iUser;
    private double iLat,iLng;
    //ExpandableTextView expandDescription;

    //db
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef, mDbDetail, mDbPreview;
    private StorageReference mStorageRef;


    public DetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_fragment,container, false);

        description = view.findViewById(R.id.txt_description);
        picknikName = view.findViewById(R.id.tvd_tempat);
        genre = view.findViewById(R.id.tvd_genre);
        username = view.findViewById(R.id.tv_user);
        alamat = view.findViewById(R.id.detail_alamat);
        category = view.findViewById(R.id.tv_category);
        time_opens = view.findViewById(R.id.tv_open);
        time_closes = view.findViewById(R.id.tv_closed);
        avgCost = view.findViewById(R.id.tv_avgCost);
        avatar = view.findViewById(R.id.user_pict);



        try {
            previewRecyclerView = view.findViewById(R.id.preview_recyclerview);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            previewRecyclerView.setLayoutManager(layoutManager);
            //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            previewRecyclerView.setHasFixedSize(true);

            // verticalRecyclerView.setLayoutManager(layoutManager);
            PreviewAdaptor adapter;

            //make recycler adapter
            adapter = new PreviewAdaptor(imageData, getContext());
            previewRecyclerView.setAdapter(adapter);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    //getting the data from another intent
        Intent I = getActivity().getIntent();
        iPost = I.getStringExtra(EXTRA_IDPOST);
        iUser = I.getStringExtra(EXTRA_IDUSER);

        //auth user
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        userId = user.getUid();

        mDbRef = mDatabase.getReference().child("user");

        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Seperti ini bisa juga utk mengambil extra intent.
//        postId = getActivity().getIntent().getExtras().getString(DetailsFragment.EXTRA_POSITION);
//        System.out.println(postId+" from intent");

        setImage();

    } // end of oncreate


    private void setDetail(DataSnapshot dataSnapshot){

        if (dataSnapshot != null){
            for (DataSnapshot ds : dataSnapshot.getChildren()){


                PostModel mPost = new PostModel();

                mPost.setId_post(ds.getValue(PostModel.class).getId_post());//Id post
                mPost.setId_user(ds.getValue(PostModel.class).getId_user());//Id user
                mPost.setPicknikName(ds.getValue(PostModel.class).getPicknikName()); //set the name
                mPost.setLocation(ds.getValue(PostModel.class).getLocation()); //set the location
                mPost.setCategory(ds.getValue(PostModel.class).getCategory()); //set the email
                mPost.setAvgCost(ds.getValue(PostModel.class).getAvgCost()); //set the email
                mPost.setDescription(ds.getValue(PostModel.class).getDescription()); //set the description
                mPost.setTime_open(ds.getValue(PostModel.class).getTime_open()); //set the open time
                mPost.setTime_closed(ds.getValue(PostModel.class).getTime_closed()); //set the closed time

                //display all the information
                picknikName.setText(mPost.getPicknikName());
                alamat.setText(mPost.getLocation());
                category.setText(mPost.getCategory());
                avgCost.setText("Rp."+ mPost.getAvgCost());
                description.setText(mPost.getDescription());
                genre.setText(mPost.getCategory());
                time_opens.setText(mPost.getTime_open());
                time_closes.setText(mPost.getTime_closed());
            }
        }


    }


    private void setUser(DataSnapshot dataSnapshot){

        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            User mUser = new User();

            mUser.setAvatar(ds.getValue(User.class).getAvatar()); //set the avatar
            mUser.setUsername(ds.getValue(User.class).getUsername()); //set the username

            //display all the information
            username.setText(mUser.getUsername());
            //time.setText(mPost.getTime_open());

            mStorageRef.child("Images").child(mUser.getAvatar()).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(getContext())
                                    .load(uri)
                                    .into(avatar);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        //getting user snapshot
        Query qUser = mDbRef.orderByChild("user_id").equalTo(iUser);

        qUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setUser(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                throw databaseError.toException(); // don't ignore errors
            }
        });


//getting Details snapshot
        mDbDetail = mDatabase.getReference().child("user").child("picknik-post");

        Query qDetail = mDbDetail.orderByChild("id_post").equalTo(iPost);
        qDetail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setDetail(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                throw databaseError.toException(); // don't ignore errors
            }
        });
    }

    public void setImage(){

        imageData = new ArrayList<>();
        imageData.add(new ImageData("https://4.bp.blogspot.com/-6COBCPKXMFw/WDMuTmS3qWI/AAAAAAAAIGk/vbZiqacTWWsCYS2_KZHU2M-i7ANJtDXagCLcB/s1600/Menu%2BWarunk%2BUpnormal%2BDepok%2BMargona.JPG"));
        imageData.add(new ImageData("http://katalogpromosi.com/wp-content/uploads/2018/05/warunk-upnormal_18082018p1.jpg"));
        imageData.add(new ImageData("https://i2.wp.com/duta.co/wp-content/uploads/2017/08/208-warunk-upnormal.jpg?fit=640%2C434&ssl=1"));
        imageData.add(new ImageData("https://3.bp.blogspot.com/-WPtA9ljlSng/WfRE1mPI0VI/AAAAAAAAEYw/_U57l8_s9OEwYdL-uMgs5bADBcrtqRrWACEwYBhgL/s1600/IMG20171027190103.jpg"));
    }


}

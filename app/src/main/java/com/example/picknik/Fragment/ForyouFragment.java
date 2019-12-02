package com.example.picknik.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.picknik.Adaptor.RecycleViewAdapter;
import com.example.picknik.Models.VerticalModel;
import com.example.picknik.Adaptor.VerticalAdapter;
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

public class ForyouFragment extends Fragment {
    private static final String TAG = "ForyouFragment";

    private ArrayList<PostModel> data;
    private ArrayList<VerticalModel> vertical;

    private VerticalAdapter adapter;
    private VerticalModel vModel;
    private RecyclerView verticalRecyclerView;
    private String id_post;


    private RecyclerView recyclerViewH;
    private RecycleViewAdapter adapterH;


    //db
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;

    public ForyouFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.foryou_fragment,container, false);

        try {
            verticalRecyclerView = view.findViewById(R.id.recycler_foryou);


            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            verticalRecyclerView.setLayoutManager(layoutManager);
            //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            verticalRecyclerView.setHasFixedSize(true);

            // verticalRecyclerView.setLayoutManager(layoutManager);

            //make vertical adapter
            adapter = new VerticalAdapter(getContext(),vertical);
            verticalRecyclerView.setAdapter(adapter);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
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

        mDbRef = mDatabase.getReference().child("user").child("picknik-post");

        setTitle();

    }

    private void setTitle(){
        Log.d(TAG, "setTitle: preparing data.");

        vertical = new ArrayList<>();

        for (int i =1; i<=3; i++){
            vModel = new VerticalModel();

            if (i == 1){
                vModel.setTitle("Trending");
                data = new ArrayList<>();

//                            vModel.setVerticalList(data);
//                            adapterH = new RecycleViewAdapter(vModel.setVerticalList(data),getContext());
//                            recyclerViewH.setAdapter(adapterH);
//                            recyclerViewH.setHasFixedSize(true);

                data.add(new PostModel("-LcAh_13yJCpjwUVbcWa","3Y7nRPussEWUPWvTGGBNHGhU5KH3","Two Cents","Jl Cimanuk No.2 , Bandung","P-LcAh_13yJCpjwUVbcWa"));

                data.add(new PostModel("-Ld0M9TlCsThrBMQFqoP","3Y7nRPussEWUPWvTGGBNHGhU5KH3","McDonald's Setiabudi","Jl Setiabudi No.88, Cicadap, Bandung","P-Ld0M9YzR39iJ7iupZnU.jpg"));

                data.add(new PostModel("-LdBtF9A77JMIe0bBkaN","3Y7nRPussEWUPWvTGGBNHGhU5KH3","Kampoeng Jazz","UNPAD, Jalan Dipati Ukur No. 35, Bandung","-LdBtF9A77JMIe0bBkaO"));

                data.add(new PostModel("-LdJC5xqdTaa90CQhK0v","3Y7nRPussEWUPWvTGGBNHGhU5KH3","Farmhouse Susu Lembang","Jl. Raya Lembang No. 108, Bandung","P-LdJC60UMAVcXRhJHGhc"));

                data.add(new PostModel("-LdbPmTTrVW1a5o93TUL","3Y7nRPussEWUPWvTGGBNHGhU5KH3","G.H. Universal Belle Vue Roof Top","Jl Dr. Setiabudi No. 376, Bandung","P-LdbOXF7qcIwNk-gvnW6"));

            }
            if (i == 2){
                vModel.setTitle("Here, Take a trip");

//                mDbRef.limitToFirst(6).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        if (dataSnapshot.exists()) {
//
//                            data = new ArrayList<>();
//                            data.clear();
//                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
//
//                                // read the index key
//                                PostModel mPost=ds.getValue(PostModel.class);
//
//
//                                System.out.println(mPost.getPicknikName()+ " for you");
//                                if (mPost.getId_post() !=null){
//                                    data.add(mPost);
//                                }else {
//                                    data.clear();
//                                }
//
//                                //display all the information
//                            }
//                            vModel.setVerticalList(data);
//
//                        }else {
//                            Toast.makeText(getContext(), "Error Occurred"
//                                    , Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(getContext(), "Opsss.... Something's wrong", Toast.LENGTH_SHORT).show();
//                    }
//                });
                data =new ArrayList<>();

                data.add(new PostModel("-LcAh_13yJCpjwUVbcWa","3Y7nRPussEWUPWvTGGBNHGhU5KH3","Two Cents","Jl Cimanuk No.2 , Bandung","P-LcAh_13yJCpjwUVbcWa"));

                data.add(new PostModel("-Ld0M9TlCsThrBMQFqoP","3Y7nRPussEWUPWvTGGBNHGhU5KH3","McDonald's Setiabudi","Jl Setiabudi No.88, Cicadap, Bandung","P-Ld0M9YzR39iJ7iupZnU.jpg"));

                data.add(new PostModel("-LdBtF9A77JMIe0bBkaN","3Y7nRPussEWUPWvTGGBNHGhU5KH3","Kampoeng Jazz","UNPAD, Jalan Dipati Ukur No. 35, Bandung","-LdBtF9A77JMIe0bBkaO"));

                data.add(new PostModel("-LdJC5xqdTaa90CQhK0v","3Y7nRPussEWUPWvTGGBNHGhU5KH3","Farmhouse Susu Lembang","Jl. Raya Lembang No. 108, Bandung","P-LdJC60UMAVcXRhJHGhc"));

                data.add(new PostModel("-LdbPmTTrVW1a5o93TUL","3Y7nRPussEWUPWvTGGBNHGhU5KH3","G.H. Universal Belle Vue Roof Top","Jl Dr. Setiabudi No. 376, Bandung","P-LdbOXF7qcIwNk-gvnW6"));

            }
            if (i == 3){
                vModel.setTitle("Event for you");

                data = new ArrayList<>();

                data.add(new PostModel("-LcAh_13yJCpjwUVbcWa","3Y7nRPussEWUPWvTGGBNHGhU5KH3","Two Cents","Jl Cimanuk No.2 , Bandung","P-LcAh_13yJCpjwUVbcWa"));

                data.add(new PostModel("-Ld0M9TlCsThrBMQFqoP","3Y7nRPussEWUPWvTGGBNHGhU5KH3","McDonald's Setiabudi","Jl Setiabudi No.88, Cicadap, Bandung","P-Ld0M9YzR39iJ7iupZnU.jpg"));

                data.add(new PostModel("-LdBtF9A77JMIe0bBkaN","3Y7nRPussEWUPWvTGGBNHGhU5KH3","Kampoeng Jazz","UNPAD, Jalan Dipati Ukur No. 35, Bandung","-LdBtF9A77JMIe0bBkaO"));

                data.add(new PostModel("-LdJC5xqdTaa90CQhK0v","3Y7nRPussEWUPWvTGGBNHGhU5KH3","Farmhouse Susu Lembang","Jl. Raya Lembang No. 108, Bandung","P-LdJC60UMAVcXRhJHGhc"));

                data.add(new PostModel("-LdbPmTTrVW1a5o93TUL","3Y7nRPussEWUPWvTGGBNHGhU5KH3","G.H. Universal Belle Vue Roof Top","Jl Dr. Setiabudi No. 376, Bandung","P-LdbOXF7qcIwNk-gvnW6"));

            }

            try {
//
                vModel.setVerticalList(data);

                vertical.add(vModel);



            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }

        }


//        for (int i = 1; i <= 3; i++) {
//
//            VerticalModel dm = new VerticalModel();
//
//            dm.setTitle("");
//
//
//            ArrayList<DataModel> data = new ArrayList<DataModel>();
//            for (int j = 1; j <= 3; j++) {
//                 data.add(new DataModel("Item " + j, "URL " + j,""));
//
//            }
//
//            dm.setVerticalList(data);
//
//            vertical.add(dm);
//
//        }
//
//        try {
//
//            dm.setVerticalList(data);
//
//            vertical.add(dm);
//
//
//
//        }catch (Exception ex){
//            System.out.println(ex.getMessage());
//        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }
}

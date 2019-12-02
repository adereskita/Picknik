package com.example.picknik.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.picknik.Adaptor.CollectionRecyclerAdapter;
import com.example.picknik.Models.CollectionModel;
import com.example.picknik.R;

import java.util.ArrayList;

public class CollectionFragment extends Fragment {
    private static final String TAG = "CollectionFragment";

    CollectionModel collection;
    ArrayList<CollectionModel> cdata;
    RecyclerView collectionRecycler;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collection_fragment,container, false);

        try {
            collectionRecycler = view.findViewById(R.id.recycler_collection);


            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            collectionRecycler.setLayoutManager(layoutManager);
            //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            // verticalRecyclerView.setLayoutManager(layoutManager);
            CollectionRecyclerAdapter adapter;

            //make vertical adapter
            adapter = new CollectionRecyclerAdapter(getContext(),cdata);
            collectionRecycler.setAdapter(adapter);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setdata();
    }

    public void setdata(){

        //category.setTitle("Trending");

        cdata = new ArrayList<>();
        cdata.add(new CollectionModel("Trending","Place","Most popular restaurant in town","100 place","https://images.pexels.com/photos/544113/pexels-photo-544113.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"));

        cdata.add(new CollectionModel("Fast Food","Fast Food","Most popular restaurant in town","123 place","https://images.pexels.com/photos/551991/pexels-photo-551991.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"));

        cdata.add(new CollectionModel("Event","Music Festival","Most popular restaurant in town","99 place","https://images.pexels.com/photos/1047442/pexels-photo-1047442.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"));

        cdata.add(new CollectionModel("Here, Romantic Place","Romantic","Most Romantic place in town","27 place","https://images.pexels.com/photos/1246956/pexels-photo-1246956.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"));

        cdata.add(new CollectionModel("Refreshing","Attraction","Most popular Refreshing in town","33 place","https://images.pexels.com/photos/1403653/pexels-photo-1403653.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"));

    }
}

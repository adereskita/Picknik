package com.example.picknik.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.picknik.Adaptor.CategoryRecyclerAdapter;
import com.example.picknik.Models.CategoryModel;
import com.example.picknik.R;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {
    private static final String TAG = "CategoryFragment";

    CategoryModel category;
    ArrayList<CategoryModel> cdata;
    RecyclerView categoryRecycler;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment,container, false);

        try {
            categoryRecycler = view.findViewById(R.id.recycler_category);


            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
            categoryRecycler.setLayoutManager(layoutManager);
            //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            // verticalRecyclerView.setLayoutManager(layoutManager);
            CategoryRecyclerAdapter adapter;

            //make vertical adapter
            adapter = new CategoryRecyclerAdapter(getContext(),cdata);
            categoryRecycler.setAdapter(adapter);
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
        cdata.add(new CategoryModel("Cafe","196 place","https://images.pexels.com/photos/544113/pexels-photo-544113.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"));

        cdata.add(new CategoryModel("Fast Food","156 place","https://images.pexels.com/photos/551991/pexels-photo-551991.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"));

        cdata.add(new CategoryModel("Indonesian Food","956 place","https://images.pexels.com/photos/1234535/pexels-photo-1234535.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"));

        cdata.add(new CategoryModel("Street Food","956 place","https://images.pexels.com/photos/1234535/pexels-photo-1234535.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"));

        cdata.add(new CategoryModel("Chinese Food","196 place","https://images.pexels.com/photos/1234535/pexels-photo-1234535.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"));

        cdata.add(new CategoryModel("Western Food","195 place","https://images.pexels.com/photos/1234535/pexels-photo-1234535.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"));

        cdata.add(new CategoryModel("Attraction","596 place","https://images.pexels.com/photos/225238/pexels-photo-225238.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"));

        cdata.add(new CategoryModel("Workspace","596 place","https://images.pexels.com/photos/1451447/pexels-photo-1451447.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"));

        cdata.add(new CategoryModel("Theme Park","916 place","https://images.pexels.com/photos/225238/pexels-photo-225238.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"));

        cdata.add(new CategoryModel("Water Park","256 place","https://images.pexels.com/photos/225238/pexels-photo-225238.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"));

        cdata.add(new CategoryModel("Zoo","356 place","https://images.pexels.com/photos/225238/pexels-photo-225238.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"));

        cdata.add(new CategoryModel("Park","156 place","https://images.pexels.com/photos/225238/pexels-photo-225238.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"));

        cdata.add(new CategoryModel("Sport Festival","96 place","https://images.pexels.com/photos/225238/pexels-photo-225238.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"));

        cdata.add(new CategoryModel("Food Festival","156 place","https://images.pexels.com/photos/225238/pexels-photo-225238.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"));

        cdata.add(new CategoryModel("Japanese Food","1956 place","https://images.pexels.com/photos/225238/pexels-photo-225238.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"));

    }
}

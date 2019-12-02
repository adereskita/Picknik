package com.example.picknik.Models;

import com.example.picknik.Utils.PostModel;

import java.util.ArrayList;

public class VerticalModel {
   private String title;

    private ArrayList<PostModel> verticalList;

    public VerticalModel() {
    }


    public VerticalModel(String title, ArrayList<PostModel> verticalList) {
        this.title = title;
        this.verticalList = verticalList;
    }


    public ArrayList<PostModel> getVerticalList() {
        return verticalList;
    }

    public void setVerticalList(ArrayList<PostModel> verticalList) {
        this.verticalList = verticalList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

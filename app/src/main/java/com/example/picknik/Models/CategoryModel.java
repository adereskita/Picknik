package com.example.picknik.Models;

public class CategoryModel {
    private String category;
    private String category_place;
    private String thumbnail;

    public CategoryModel(String category, String category_place, String thumbnail) {
        this.category = category;
        this.category_place = category_place;
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory_place() {
        return category_place;
    }

    public void setCategory_place(String category_place) {
        this.category_place = category_place;
    }
}

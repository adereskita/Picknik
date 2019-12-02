package com.example.picknik.Models;

public class CollectionModel {

    private String collection;
    private String category;
    private String desc;
    private String collection_place;
    private String thumbnail;

    public CollectionModel(String collection, String category, String desc, String collection_place, String thumbnail) {
        this.collection = collection;
        this.category = category;
        this.desc = desc;
        this.collection_place = collection_place;
        this.thumbnail = thumbnail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCollection_place() {
        return collection_place;
    }

    public void setCollection_place(String collection_place) {
        this.collection_place = collection_place;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}

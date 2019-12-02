package com.example.picknik.Utils;

public class PostModel {
    private String id_user,id_post,PicknikName,phone,location,time_open,time_closed
            ,type,category,owner,avgCost,imageSrc,imgType,description;
    private String id_rating, username, title_rating ,txt_rating;
    private  float  rating;
    private double lat,lng;

    public PostModel() {
    }

    //data longitude latitude
    public PostModel(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public PostModel(String id_post, String id_user, String PicknikName, String location, String imageSrc) {
        this.id_post = id_post;
        this.id_user = id_user;
        this.PicknikName = PicknikName;
        this.location = location;
        this.imageSrc = imageSrc;
    }

    //add review
    public PostModel(String id_rating, String id_user,String username, String title_rating,
                     String txt_rating, float rating) {
        this.id_rating = id_rating;
        this.id_user = id_user;
        this.username = username;
        this.title_rating = title_rating;
        this.txt_rating = txt_rating;
        this.rating = rating;
    }

    public PostModel(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    //input
    public PostModel(String id_user,String id_post, String PicknikName, String phone,
                     String location,double lat,double lng, String type, String category,
                     String time_open,String time_closed,String avgCost,
                     String owner, String imageSrc, String description) {
        this.id_user = id_user;
        this.id_post = id_post;
        this.PicknikName = PicknikName;
        this.phone = phone;
        this.location = location;
        this.lat = lat;
        this.lng = lng;
        this.type = type;
        this.category = category;
        this.time_open = time_open;
        this.time_closed = time_closed;
        this.avgCost = avgCost;
        this.owner = owner;
        this.imageSrc = imageSrc;
        this.description = description;
    }



    //list profile review
    public PostModel(String id_post, String PicknikName, String category, String location
            , String imageSrc,String txt_rating, float rating) {
        this.id_post = id_post;
        this.PicknikName = PicknikName;
        this.category = category;
        this.location = location;
        this.imageSrc = imageSrc;
        this.txt_rating = txt_rating;
        this.rating = rating;
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getTime_open() {
        return time_open;
    }

    public void setTime_open(String time_open) {
        this.time_open = time_open;
    }

    public String getTime_closed() {
        return time_closed;
    }

    public void setTime_closed(String time_closed) {
        this.time_closed = time_closed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle_rating() {
        return title_rating;
    }

    public void setTitle_rating(String title_rating) {
        this.title_rating = title_rating;
    }

    public String getId_rating() {
        return id_rating;
    }

    public void setId_rating(String id_rating) {
        this.id_rating = id_rating;
    }

    public String getTxt_rating() {
        return txt_rating;
    }

    public void setTxt_rating(String txt_rating) {
        this.txt_rating = txt_rating;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_post() {
        return id_post;
    }

    public void setId_post(String id_post) {
        this.id_post = id_post;
    }

    public String getPicknikName() {
        return PicknikName;
    }

    public void setPicknikName(String picknikName) {
        PicknikName = picknikName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAvgCost() {
        return avgCost;
    }

    public void setAvgCost(String avgCost) {
        this.avgCost = avgCost;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}

package com.example.picknik.Models;

public class ProfileModel {
    private String namatempat,kategori,alamat,imgSrc;
    private  int  rating;

    public ProfileModel() {
    }

    public ProfileModel(String namatempat, String kategori, String alamat, String imgSrc) {
        this.namatempat = namatempat;
        this.kategori = kategori;
        this.alamat = alamat;
        this.imgSrc = imgSrc;
    }

    public ProfileModel(String namatempat, String kategori, String alamat, String imgSrc, int rating) {
        this.namatempat = namatempat;
        this.kategori = kategori;
        this.alamat = alamat;
        this.imgSrc = imgSrc;
        this.rating = rating;
    }

    public String getNamatempat() {
        return namatempat;
    }

    public void setNamatempat(String namatempat) {
        this.namatempat = namatempat;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

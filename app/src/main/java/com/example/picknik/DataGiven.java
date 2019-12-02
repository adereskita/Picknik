package com.example.picknik;

public enum DataGiven {

    testUser1("username1", "password"),
    testUser2("adee.reskita@yahoo.com", "adereskita12");

    private String email;
    private String password;

    DataGiven(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return email;
    }

}

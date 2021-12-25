package com.id1720430.blooddonationapp.usertype;

public class User {
    String name, bloodgroup, nationalid, email, gender, phonenumber,profilepictureurl,type, homeaddress, postalcode;

    public User(String name, String bloodgroup, String nationalid, String email, String gender, String phonenumber, String profilepictureurl, String type, String homeaddress, String postalcode) {
        this.name = name;
        this.bloodgroup = bloodgroup;
        this.nationalid = nationalid;
        this.email = email;
        this.gender = gender;
        this.phonenumber = phonenumber;
        this.profilepictureurl = profilepictureurl;
        this.type = type;
        this.homeaddress = homeaddress;
        this.postalcode = postalcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getNationalid() {
        return nationalid;
    }

    public void setNationalid(String nationalid) {
        this.nationalid = nationalid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getProfilepictureurl() {
        return profilepictureurl;
    }

    public void setProfilepictureurl(String profilepictureurl) {
        this.profilepictureurl = profilepictureurl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHomeaddress() {
        return homeaddress;
    }

    public void setHomeaddress(String homeaddress) {
        this.homeaddress = homeaddress;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }
}

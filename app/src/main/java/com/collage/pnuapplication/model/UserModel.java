package com.collage.pnuapplication.model;

public class UserModel {
    private String id = "";
    private String name = "";
    private String ssn = "";
    private String dob = "";
    private String mail = "";
    private String phone = "";
    private String gender = "";
    private String userName = "";
    private String password = "";
    private String barcode = "";
    private String type  = "";
    
    public UserModel(String id, String name, String ssn, String dob, String mail, String phone, String gender, String userName, String password, String barcode, String type) {
        this.id = id;
        this.name = name;
        this.ssn = ssn;
        this.dob = dob;
        this.mail = mail;
        this.phone = phone;
        this.gender = gender;
        this.userName = userName;
        this.password = password;
        this.barcode = barcode;
        this.type = type;
    }

    public UserModel() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

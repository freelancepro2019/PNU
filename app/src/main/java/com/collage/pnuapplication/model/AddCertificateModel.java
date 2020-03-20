package com.collage.pnuapplication.model;

public class AddCertificateModel {
    private String id;
    private String admin_id;
    private String admin_name;
    private String image;
    private String time;

    public AddCertificateModel() {
    }

    public AddCertificateModel(String id, String admin_id, String admin_name, String image, String time) {
        this.id = id;
        this.admin_id = admin_id;
        this.admin_name = admin_name;
        this.image = image;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

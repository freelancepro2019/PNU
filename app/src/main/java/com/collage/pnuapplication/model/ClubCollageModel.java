package com.collage.pnuapplication.model;

public class ClubCollageModel {


    private String id   = "";
    private String name = "";
    private String desc = "";
    private String image  = "";



    // 1 for collage 2 for club

    private String type = "";


    public ClubCollageModel() {
    }

    public ClubCollageModel(String id, String name, String desc, String image) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.image = image;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

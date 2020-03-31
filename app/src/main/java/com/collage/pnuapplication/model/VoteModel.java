package com.collage.pnuapplication.model;

public class VoteModel {
    private String id = "";
    private String clubId = "";
    private double voteValue = 0;
    private String userId = "";


    public VoteModel() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public double getVoteValue() {
        return voteValue;
    }

    public void setVoteValue(double voteValue) {
        this.voteValue = voteValue;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

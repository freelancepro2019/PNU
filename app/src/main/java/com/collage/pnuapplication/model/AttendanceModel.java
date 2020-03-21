package com.collage.pnuapplication.model;

import java.io.Serializable;
import java.util.List;

public class AttendanceModel implements Serializable {

    private String course_id;
    private String course_name;
    private String course_image;
    private List<AttendanceUser> users;

    public AttendanceModel() {
    }


    public AttendanceModel(String course_id, String course_name, String course_image, List<AttendanceUser> users) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.course_image = course_image;
        this.users = users;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public List<AttendanceUser> getUsers() {
        return users;
    }

    public void setUsers(List<AttendanceUser> users) {
        this.users = users;
    }

    public String getCourse_image() {
        return course_image;
    }

    public void setCourse_image(String course_image) {
        this.course_image = course_image;
    }
}

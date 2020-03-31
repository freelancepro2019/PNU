package com.collage.pnuapplication.model;

public class ReserveModel {



    private String id = "";
    private String userId = "";
    private String time = "";
    private String courseId = "";

    private CourseModel course ;


    public ReserveModel() {

    }


    public String getCourseId() {

        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public CourseModel getCourse() {
        return course;
    }

    public void setCourse(CourseModel course) {
        this.course = course;
    }
}

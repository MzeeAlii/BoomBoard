package creationsofali.boomboard.datamodels;

/**
 * Created by ali on 5/13/17.
 */

public class Student {
    private String collegeFull, collegeAbr, facultyFull, facultyAbr, email, uid, pushToken;
    private int yearOfStudy;

    public Student() {
        // default
        this.yearOfStudy = 0;
    }

    public void setCollegeFull(String collegeFull) {
        this.collegeFull = collegeFull;
    }

    public String getCollegeAbr() {
        return collegeAbr;
    }

    public void setCollegeAbr(String collegeAbr) {
        this.collegeAbr = collegeAbr;
    }

    public String getFacultyAbr() {
        return facultyAbr;
    }

    public String getCollegeFull() {
        return collegeFull;
    }

    public String getFacultyFull() {
        return facultyFull;
    }

    public void setFacultyFull(String facultyFull) {
        this.facultyFull = facultyFull;
    }

    public void setFacultyAbr(String facultyAbr) {
        this.facultyAbr = facultyAbr;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }
}

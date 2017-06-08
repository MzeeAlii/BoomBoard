package creationsofali.boomboard.datamodels;

/**
 * Created by ali on 5/22/17.
 */

public class Staff {

    private String name, title, photoUrl, collegeAbbr, collegeFull, forte, phone;
    // private int totalPosts;

    public Staff() {
        // default constructor
        // this.totalPosts = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCollegeAbbr() {
        return collegeAbbr;
    }

    public void setCollegeAbbr(String collegeAbbr) {
        this.collegeAbbr = collegeAbbr;
    }

    public String getCollegeFull() {
        return collegeFull;
    }

    public void setCollegeFull(String collegeFull) {
        this.collegeFull = collegeFull;
    }

    public String getForte() {
        return forte;
    }

    public void setForte(String forte) {
        this.forte = forte;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

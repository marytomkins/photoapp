package com.example.photos22;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Picture implements Serializable {
    public String path;
    public String location;
    public ArrayList<String> people;

    public Picture(String picturePath, String location, ArrayList<String> people) {
        this.path = picturePath;
        this.location = location;
        this.people = people;
    }

    public String getPath() {
        return path;
    }

    public String getLocation() {
        return location;
    }

    public ArrayList<String> getPeople() {
        return people;
    }

    public void setLocation(String locatio) {
        this.location = locatio;
    }

    public void resetLocation(){ this.location = ""; }

    public void resetPeople(){
        people.clear();
    }

}

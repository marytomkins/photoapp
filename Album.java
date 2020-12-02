package com.example.photos22;


import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    private String name;
    public ArrayList<Picture> pictures = new ArrayList<Picture>();

    public Album(String result, ArrayList<Picture> pics) {
        this.name = result;
        this.pictures = pics;
    }

    public String getName() {
        return name;
    }

    public void changename(String rename) {
        name = rename;
    }

    public ArrayList<Picture> getPictures() {
        return pictures;
    }

    public void addPicture(Picture pic){
        pictures.add(pic);
    }

}

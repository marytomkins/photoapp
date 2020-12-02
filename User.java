package com.example.photos22;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    public ArrayList<Album> appalbums;
    public static final String fileName = "userfile.ser";

    public ArrayList<Album> getAppAlbums() {
        return appalbums;
    }

    public void addAppAlbum(Album album) {
        appalbums.add(album);
    }
    public static User loadFromDisk(Context context){
        User pa = null;
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            pa = (User) ois.readObject();

            if (pa.appalbums == null) {
                pa.appalbums = new ArrayList<Album>();
            }
            fis.close();
            ois.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
        return pa;
    }


    public void saveToDisk(Context context){
        ObjectOutputStream oos;
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

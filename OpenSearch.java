package com.example.photos22;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class OpenSearch extends AppCompatActivity {

    final Context context = this;
    User currentuser;
    private Button go;
    private EditText search_loc;
    private EditText search_person;
    private ListView listView;
    public static ArrayList<String> pathsinlist = new ArrayList<String>();
    public static ArrayList<Picture> searched_photos = new ArrayList<Picture>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        currentuser = User.loadFromDisk(this);
        if(currentuser == null) {
            currentuser = new User();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_search);

        go = findViewById(R.id.go);
        search_loc = findViewById(R.id.locsearch);
        search_person = findViewById(R.id.personsearch);
        listView = findViewById(R.id.listview);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searched_photos.clear();
                CustomAdapter customAdapter = new CustomAdapter();
                listView.setAdapter(customAdapter);

                if(search_loc.getText().length() == 0 && search_person.getText().length() == 0){

                }else{
                    if(search_loc.getText().length() > 0){
                        for(int albumindex = 0; albumindex < currentuser.appalbums.size(); albumindex++) {
                            for (int photoindex = 0; photoindex < currentuser.appalbums.get(albumindex).pictures.size(); photoindex++) {
                                if (currentuser.appalbums.get(albumindex).pictures.get(photoindex).location.startsWith(search_loc.getText().toString())) {
                                    searched_photos.add(currentuser.appalbums.get(albumindex).pictures.get(photoindex));
                                    customAdapter = new CustomAdapter();
                                    listView.setAdapter(customAdapter);
                                    pathsinlist.add(currentuser.appalbums.get(albumindex).pictures.get(photoindex).getPath());
                                }
                            }
                        }
                    }
                    if(search_person.getText().length() > 0){
                        for(int albumindex = 0; albumindex < currentuser.appalbums.size(); albumindex++) {
                            for (int photoindex = 0; photoindex < currentuser.appalbums.get(albumindex).pictures.size(); photoindex++) {
                                for(int tagindex = 0; tagindex < currentuser.appalbums.get(albumindex).pictures.get(photoindex).getPeople().size(); tagindex++){
                                    if(currentuser.appalbums.get(albumindex).pictures.get(photoindex).people.get(tagindex).startsWith(search_person.getText().toString())){
                                        if(checkforpathinlist(currentuser.appalbums.get(albumindex).pictures.get(photoindex).getPath()) == 0) {
                                            searched_photos.add(currentuser.appalbums.get(albumindex).pictures.get(photoindex));
                                            customAdapter = new CustomAdapter();
                                            listView.setAdapter(customAdapter);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    search_loc.setText("");
                    search_person.setText("");
                }
            }
        });
    }

    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return searched_photos.size();
        }
        @Override
        public Object getItem(int i) {
            return null;
        }
        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.activity_photorow,null);

            //getting view in row_data
            TextView name = view1.findViewById(R.id.path);
            ImageView image = view1.findViewById(R.id.imageeee);

            name.setText(searched_photos.get(i).getPath());
            image.setImageBitmap(BitmapFactory.decodeFile(searched_photos.get(i).getPath()));
            return view1;
        }
    }

    private int checkforpathinlist(String path){
        for(int i = 0; i < pathsinlist.size(); i++){
            if(pathsinlist.get(i).compareTo(path) == 0) {
                return 1;
            }
        }
        return 0;
    }

}
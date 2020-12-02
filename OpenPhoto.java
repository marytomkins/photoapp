package com.example.photos22;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class OpenPhoto extends AppCompatActivity {

    User currentuser;
    final Context context = this;
    public static TextView caption;
    public static ImageView photodisplay;
    public static Button edittag;
    public static Button deltag;
    public static Button left;
    public static Button right;
    public static Button backtoalbum;
    public static TextView locationtag;
    public static TextView peopletag;
    public static int albumindex;
    public static int photoindex;
    String[] listitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        currentuser = User.loadFromDisk(this);
        if(currentuser == null) {
            currentuser = new User();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_photo);

        caption = findViewById(R.id.caption);
        photodisplay = findViewById(R.id.photodisplay);
        locationtag = findViewById(R.id.locationtag);
        peopletag = findViewById(R.id.peopletag);
        edittag = findViewById(R.id.edittag);
        deltag = findViewById(R.id.deletetag);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        backtoalbum = findViewById(R.id.backtoalbum);

        albumindex = (int)getIntent().getSerializableExtra("albumindex");
        photoindex = (int)getIntent().getSerializableExtra("photoindex");
        Log.d("photoindex", String.valueOf(photoindex));

        caption.setText(currentuser.appalbums.get(albumindex).pictures.get(photoindex).getPath());
        photodisplay.setImageBitmap(BitmapFactory.decodeFile(currentuser.appalbums.get(albumindex).pictures.get(photoindex).getPath()));

        if((currentuser.appalbums.get(albumindex).pictures.get(photoindex).getLocation()) != null){
            locationtag.append(" " + currentuser.appalbums.get(albumindex).pictures.get(photoindex).getLocation());
        }
        if(currentuser.appalbums.get(albumindex).pictures.get(photoindex).getPeople().size() > 0){
            for(int i = 0; i < currentuser.appalbums.get(albumindex).pictures.get(photoindex).getPeople().size(); i++) {
                peopletag.append(" " + currentuser.appalbums.get(albumindex).pictures.get(photoindex).getPeople().get(i));
            }
        }

        backtoalbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OpenAlbum.class);
                intent.putExtra("index", albumindex);
                startActivity(intent);
            }
        });

        edittag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit tags:");

                final EditText locbox = new EditText(context);
                locbox.setHint("Enter location");
                final EditText personbox = new EditText(context);
                personbox.setHint("Enter one person at a time");

                LinearLayout ll=new LinearLayout(context);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(locbox);
                ll.addView(personbox);
                builder.setView(ll);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String newloc = locbox.getText().toString();
                        String newperson = personbox.getText().toString();

                        if (newloc.length() > 0) {
                            locationtag.setText("Location: " + newloc);
                        }
                        if (newperson.length() > 0) {
                            if (peopletag.getText().length()==0) {
                                peopletag.setText("People: " + newperson);
                            } else {
                                peopletag.append(" " + newperson);
                            }
                        }
                        setTags(newloc, newperson);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });


        deltag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listitems = new String[]{"Location", "People"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete a tag:");

                builder.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if(listitems[i] == "Location"){
                            locationtag.setText("");
                            deleteloc();
                        }else{
                            peopletag.setText("");
                            deletepeep();
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpenPhoto.this, OpenPhoto.class);
                intent.putExtra("albumindex", albumindex);
                if(photoindex == 0){
                    intent.putExtra("photoindex", currentuser.appalbums.get(albumindex).pictures.size()-1);
                }else {
                    intent.putExtra("photoindex", photoindex + 1);
                }
                startActivity(intent);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpenPhoto.this, OpenPhoto.class);
                intent.putExtra("albumindex", albumindex);
                if(photoindex == currentuser.appalbums.get(albumindex).pictures.size()-1){
                    intent.putExtra("photoindex", 0);
                }else {
                    intent.putExtra("photoindex", photoindex + 1);
                }
                startActivity(intent);
            }
        });
    }

    public void setTags(String loc, String person){
        Log.d("loc", loc);
        Log.d("person", person);
        if(loc.length()>0){
            currentuser.appalbums.get(albumindex).pictures.get(photoindex).setLocation(loc);
        }
        if(person.length()>0) {
            currentuser.appalbums.get(albumindex).pictures.get(photoindex).people.add(person);
        }
        currentuser.saveToDisk(context);
    }

    public void deleteloc(){
        currentuser.appalbums.get(albumindex).pictures.get(photoindex).resetLocation();
        currentuser.saveToDisk(context);
    }

    public void deletepeep(){
        currentuser.appalbums.get(albumindex).pictures.get(photoindex).resetPeople();
        currentuser.saveToDisk(context);
    }
}
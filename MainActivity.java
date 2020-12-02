package com.example.photos22;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.AlertDialog;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final Context context = this;
    User currentuser;
    private Button delbutton;
    private Button addbutton;
    private Button renamebut;
    private Button openbutton;
    private Button search;
    public static TextView text;
    public static ListView albumNames;
    public static ArrayList<String> display_albumlist = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        display_albumlist.clear();

        currentuser = User.loadFromDisk(this);
        if(currentuser == null) {
            currentuser = new User();
        }

        if (currentuser.appalbums == null) {
            currentuser.appalbums = new ArrayList<Album>();
        }else{
            for(int i = 0; i < currentuser.appalbums.size(); i++){
               display_albumlist.add(currentuser.appalbums.get(i).getName());
               Log.d("album", currentuser.appalbums.get(i).getName());
            }
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = findViewById(R.id.search);
        openbutton = findViewById(R.id.openbut);
        delbutton = findViewById(R.id.delbutton);
        addbutton = findViewById(R.id.addbutton);
        renamebut = findViewById(R.id.renamebut);
        text = findViewById(R.id.textView2);
        albumNames = (ListView)findViewById(R.id.listview);
        albumNames.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        albumNames.setSelection(0);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, display_albumlist);
        albumNames.setAdapter(arrayAdapter);

        openbutton.setEnabled(false);
        delbutton.setEnabled(false);
        renamebut.setEnabled(false);


        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddAlbum();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearch();
            }
        });

        albumNames.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                delbutton.setEnabled(true);
                renamebut.setEnabled(true);
                openbutton.setEnabled(true);

                albumNames.requestFocusFromTouch();
                albumNames.setSelection(position);

                final int pos = position;

                openbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openAlbum(pos);
                    }
                });

                delbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentuser.appalbums.remove(pos);
                        currentuser.saveToDisk(context);
                        display_albumlist.remove(pos);
                        albumNames.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, display_albumlist));
                        openbutton.setEnabled(false);
                        delbutton.setEnabled(false);
                        renamebut.setEnabled(false);
                    }
                });

                renamebut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Rename: "+ display_albumlist.get(pos));

                        final EditText input = new EditText(context);
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String renamed = input.getText().toString();
                                currentuser.appalbums.get(pos).changename(renamed);
                                currentuser.saveToDisk(context);
                                display_albumlist.set(pos, renamed);
                                albumNames.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, display_albumlist));
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();

                        openbutton.setEnabled(false);
                        delbutton.setEnabled(false);
                        renamebut.setEnabled(false);
                    }
                });

            }

        });

    }


    public void openAlbum(int pos) {
        Intent intent = new Intent(this, OpenAlbum.class);
        intent.putExtra("index", pos);
        startActivity(intent);
    }

    public void openAddAlbum(){
        Intent intent = new Intent(this, AddAlbum.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if (resultCode == 1) {
                String result = data.getStringExtra(AddAlbum.EXTRA);
                text.setText("Added " + result + ".");
                ArrayList<Picture> pics = new ArrayList<Picture>();
                Album alb = new Album(result, pics);

                display_albumlist.add(alb.getName());
                currentuser.addAppAlbum(alb);
                currentuser.saveToDisk(context);



                albumNames.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, display_albumlist));

            }
        }
    }

    public void openSearch(){
        Intent intent = new Intent(this, OpenSearch.class);
        startActivity(intent);
    }


}
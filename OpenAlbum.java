package com.example.photos22;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import java.util.ArrayList;

public class OpenAlbum extends AppCompatActivity {

    User currentuser;
    final Context context = this;
    private TextView title;
    private Button add;
    private Button delete;
    private Button open;
    private Button move;
    private Button home;
    public static ListView photos;
    public static ArrayAdapter<Picture> arrayAdapter;
    public static ArrayList<Picture> photolist = new ArrayList<Picture>();
    public static Album alb;
    public static int index;
    String[] possAlbums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        photolist.clear();

        currentuser = User.loadFromDisk(this);
        if(currentuser == null) {
            currentuser = new User();
        }

        index = (int)getIntent().getSerializableExtra("index");

        if (currentuser.appalbums.get(index).pictures.size() > 0) {
            for(int i = 0; i < currentuser.appalbums.get(index).pictures.size(); i++){
                photolist.add(currentuser.appalbums.get(index).pictures.get(i));
                Log.d("picture", currentuser.appalbums.get(index).pictures.get(i).getPath());
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_album);

        title = findViewById(R.id.title);
        add = findViewById(R.id.addphoto);
        delete = findViewById(R.id.deletephoto);
        open = findViewById(R.id.openphoto);
        move = findViewById(R.id.movephoto);
        home = findViewById(R.id.backtohome);
        photos = findViewById(R.id.listview);
        photos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        photos.setSelection(0);
        CustomAdapter customAdapter = new CustomAdapter();
        photos.setAdapter(customAdapter);

        title.setText(currentuser.appalbums.get(index).getName());

        //add.setEnabled(true);
        delete.setEnabled(false);
        open.setEnabled(false);
        move.setEnabled(false);

        possAlbums = new String[currentuser.appalbums.size()];
        for(int i = 0; i < currentuser.appalbums.size(); i++){
            possAlbums[i] = currentuser.appalbums.get(i).getName();
        }

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);

            }
        });

        photos.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                delete.setEnabled(true);
                open.setEnabled(true);
                move.setEnabled(true);

                photos.requestFocusFromTouch();
                photos.setSelection(position);

                final int pos = position;

                open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openPhoto(pos);
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentuser.appalbums.get(index).pictures.remove(pos);
                        currentuser.saveToDisk(context);
                        photolist.remove(pos);
                        CustomAdapter customAdapter = new CustomAdapter();
                        photos.setAdapter(customAdapter);
                        open.setEnabled(false);
                        delete.setEnabled(false);
                        move.setEnabled(false);

                    }
                });

                move.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Select album:");

                        builder.setSingleChoiceItems(possAlbums, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                Log.d("moving from", currentuser.appalbums.get(index).getName());
                                Log.d("moving to", currentuser.appalbums.get(i).getName());
                                currentuser.appalbums.get(i).addPicture(currentuser.appalbums.get(index).pictures.get(pos));
                                currentuser.appalbums.get(index).pictures.remove(pos);
                                currentuser.saveToDisk(context);
                                photolist.remove(pos);
                                CustomAdapter customAdapter = new CustomAdapter();
                                photos.setAdapter(customAdapter);
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                });
            }
        });
    }

    public void openPhoto(int pos){
        Intent intent = new Intent(this, OpenPhoto.class);
        intent.putExtra("albumindex", index);
        intent.putExtra("photoindex", pos);
        startActivity(intent);
    }


    //ADDS PHOTO TO PHOTO LIST
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Picture pic = new Picture(picturePath, "", new ArrayList<String>());
            currentuser.appalbums.get(index).addPicture(pic);
            currentuser.saveToDisk(context);
            photolist.add(pic);
            CustomAdapter customAdapter = new CustomAdapter();
            photos.setAdapter(customAdapter);
        }
    }

    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return photolist.size();
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

            name.setText(photolist.get(i).getPath());
            image.setImageBitmap(BitmapFactory.decodeFile(photolist.get(i).getPath()));
            return view1;
        }
    }

}

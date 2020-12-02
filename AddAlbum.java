package com.example.photos22;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AddAlbum extends AppCompatActivity {

    public static final String EXTRA = "com.example.photos22.EXTRA";
    private Button cancelbut;
    private Button donebut;
    private EditText albumname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_album);

        cancelbut = findViewById(R.id.cancelbut);
        donebut = findViewById(R.id.donebut);
        albumname = findViewById(R.id.searchbar);

        cancelbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHome();
            }
        });

        donebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(albumname.getText().toString().matches("")){
                    albumname.setError("Enter Album Name");
                }else {
                    backToHomeWAlbumName(albumname.getText().toString());
                }
            }
        });
    }

    public void backToHome(){                                       //CANCEL
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void backToHomeWAlbumName(String name){                         //ADD
        Intent intent = new Intent();
        intent.putExtra(EXTRA, name);
        setResult(1, intent);
        finish();
    }
}

package com.example.fa_dhruvbakshi_c0846368_android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton plusBtn, directionBtn;
    DatabaseActivity DB;
    ArrayList<String> id, name, lati, lng;
    ViewAdapter viewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.list);
        plusBtn = findViewById(R.id.plusBtn);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);

            }
        });
        DB = new DatabaseActivity(MainActivity.this);
        id = new ArrayList<>();
        name = new ArrayList<>();
        lati = new ArrayList<>();
        lng = new ArrayList<>();
        displaydata();
        viewAdapter = new ViewAdapter(MainActivity.this, MainActivity.this, id, name, lati, lng);
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        directionBtn = findViewById(R.id.directBtn);
        directionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    void displaydata() {
        Cursor cursor = DB.readData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Products Found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                lati.add(cursor.getString(2));
                lng.add(cursor.getString(3));


            }
        }
    }



}
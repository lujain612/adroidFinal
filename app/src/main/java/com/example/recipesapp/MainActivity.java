package com.example.recipesapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipesapp.home.HomeFragment;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // ← must match layout file name

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
    }
}

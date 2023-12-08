package com.orcs.sheycakeapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orcs.sheycakeapp.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewCategoryList;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        // Set the text of textView3 to the user's email
        TextView textView3 = findViewById(R.id.textView3);
        if (textView3 != null) {
            textView3.setText("Welcome, " + mAuth.getCurrentUser().getEmail());
        }

        findViewById(R.id.supportButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowDetailsActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.settingButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartListActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profileButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
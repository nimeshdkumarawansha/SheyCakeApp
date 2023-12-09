package com.orcs.sheycakeapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.orcs.sheycakeapp.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static final String TAG = MainActivity.class.getName();
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewCategoryList;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            redirectToLoginPage();
        }

        firebaseFirestore.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map<String, Object> products = document.getData();

                                LinearLayout linearLayoutCart = findViewById(R.id.productList);
                                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                                View view1 = inflater.inflate(R.layout.fragment_category_view, null);

                                for (Map.Entry<String, Object> entry : products.entrySet()) {
                                    if (entry.getKey().equals("name")) {
                                        TextView textView = view1.findViewById(R.id.textView8);
                                        textView.setText(entry.getValue().toString());
                                    }
                                    if (entry.getKey().equals("price")) {
                                        TextView textView = view1.findViewById(R.id.textView9);
                                        textView.setText(entry.getValue().toString());
                                    }
                                    if (entry.getKey().equals("qty")) {
                                        TextView textView = view1.findViewById(R.id.textView11);
                                        textView.setText(entry.getValue().toString());
                                    }
                                }
                                linearLayoutCart.addView(view1);
                            }
                        }
                    }
                });

        TextView userEmail = findViewById(R.id.textView3);
        TextView userName = findViewById(R.id.textView2);
        if (userEmail != null) {
            userEmail.setText(mAuth.getCurrentUser().getEmail());
        }

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
            if (userName != null) {
                userName.setText(sharedPreferences.getString("name", ""));
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "No shared pref file: ", e);
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

        findViewById(R.id.logout_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                redirectToLoginPage();
            }
        });

    }

    private void redirectToLoginPage() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
package com.orcs.sheycakeapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.orcs.sheycakeapp.R;

public class MainActivity extends AppCompatActivity {
private RecyclerView.Adapter adapter;
private RecyclerView recyclerViewCategoryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewCategory();

    }
    private void recyclerViewCategory() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategoryList = findViewById(R.id.recyclerViewCategoryList);
        recyclerViewCategoryList.setLayoutManager(layoutManager);
    }
}
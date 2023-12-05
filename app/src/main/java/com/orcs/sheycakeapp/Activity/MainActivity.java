package com.orcs.sheycakeapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.orcs.sheycakeapp.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
private RecyclerView.Adapter adapter;
private RecyclerView recyclerViewCategoryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
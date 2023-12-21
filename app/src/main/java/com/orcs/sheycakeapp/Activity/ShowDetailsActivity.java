package com.orcs.sheycakeapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orcs.sheycakeapp.R;

public class ShowDetailsActivity extends AppCompatActivity {
    public static final String TAG = ShowDetailsActivity.class.getName();
    String productId = getIntent().getStringExtra("PRODUCT_ID");
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference productRef = db.collection("products").document(productId);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

    TextView productNameText = findViewById(R.id.titleText);
        productNameText.setText(getIntent().getStringExtra("PRODUCT_ID"));

        productRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Document found, you can access the data
                    String productName = document.getString("name");
                    // Assuming "productName" is a field in your Firestore document
                    // You can replace it with the actual field name you want to retrieve

                    // Update your TextView
                    productNameText.setText(productName);
                } else {
                }
            } else {
                // Handle errors
                Exception e = task.getException();
                Log.i(TAG,e.toString());
            }
        });
        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowDetailsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.addToCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShowDetailsActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
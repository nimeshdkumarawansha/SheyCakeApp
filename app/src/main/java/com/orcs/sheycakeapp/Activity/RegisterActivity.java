package com.orcs.sheycakeapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.orcs.sheycakeapp.R;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = RegisterActivity.class.getName();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        EditText nameEditText = findViewById(R.id.inputName);
        EditText emailEditText = findViewById(R.id.inputEmail);
        EditText passwordEditText = findViewById(R.id.inputPassword);
        EditText mobileEditText = findViewById(R.id.inputMobile);

        // register function
        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String mobile = mobileEditText.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(mobile)) {
                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(email)) {
                    Toast.makeText(RegisterActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidMobile(mobile)) {
                    Toast.makeText(RegisterActivity.this, "Invalid mobile format", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // User registration successful
                                Log.i(TAG, "createUserWithEmail:success");

                                // Save user data to Firestore
                                saveUserDataToFirestore(name, email, mobile);

                                // Clear input fields
                                clearInputFields();

                                // Display toast message
                                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                                // Redirect to the login activity
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // User registration failed, handle the error
                                Log.i(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            private void saveUserDataToFirestore(String name, String email, String mobile) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                String userId = firebaseAuth.getCurrentUser().getUid();

                // Create a user object with the required fields
                Map<String, Object> user = new HashMap<>();
                user.put("name", name);
                user.put("email", email);
                user.put("mobile", mobile);

                // Add the user to the "users" collection in Firestore
                db.collection("users").document(userId)
                        .set(user)
                        .addOnSuccessListener(documentReference -> Log.i(TAG, "User data added to Firestore"))
                        .addOnFailureListener(e -> Log.e(TAG, "Error adding user data to Firestore", e));
            }

            private void clearInputFields() {
                nameEditText.setText("");
                emailEditText.setText("");
                passwordEditText.setText("");
                mobileEditText.setText("");
            }

            private boolean isValidEmail(String email) {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
            }

            private boolean isValidMobile(String mobile) {
                return mobile.matches("\\d{10}"); // Assuming a valid mobile number has 10 digits
            }
        });

        // move to login activity
        findViewById(R.id.loginAccountText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
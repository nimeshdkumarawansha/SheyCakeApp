package com.orcs.sheycakeapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.orcs.sheycakeapp.R;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = RegisterActivity.class.getName();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        String name = findViewById(R.id.inputName).toString();
        String email = findViewById(R.id.inputEmail).toString();
        String password = findViewById(R.id.inputPassword).toString();
        String mobile = findViewById(R.id.inputMobile).toString();

        //  register function
        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, task -> {
                            if (task.isSuccessful()) {
                                Log.i(TAG, "createUserWithEmail:success");
                            } else {
                                Log.i(TAG, "createUserWithEmail:failure");
                            }
                        });
            }
        });

        //  move login activity
        findViewById(R.id.loginAccountText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
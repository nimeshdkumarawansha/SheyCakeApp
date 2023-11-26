package com.orcs.sheycakeapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.orcs.sheycakeapp.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.buttonGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SignInRequest = BeginSignInRequest.builder()
//                        .setGoogleIdTokenRequestOptions(GoogleIdTokenRequestOptions.builder()
//                                .setSupported(true)
//                                // Your server's client ID, not your Android client ID.
//                                .setServerClientId(getString(R.string.default_web_client_id))
//                                // Only show accounts previously used to sign in.
//                                .setFilterByAuthorizedAccounts(true)
//                                .build())
//                        .build();

            }
        });

        //  move register activity
        findViewById(R.id.createNewAccountText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
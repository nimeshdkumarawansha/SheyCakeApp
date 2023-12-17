package com.orcs.sheycakeapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.orcs.sheycakeapp.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {
    public static final String TAG = UserProfileActivity.class.getName();
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImage;
    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView mobileTextView;
    private FloatingActionButton floatingActionButton;
    private DatabaseReference databaseReference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Uri profileImageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePicsRef;
    String uid = mAuth.getCurrentUser().getUid();
    EditText editNameTxt;
    EditText editEmailTxt;
    EditText editMobileTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        profileImage = findViewById(R.id.profileImage);
        floatingActionButton = findViewById(R.id.camera);
        usernameTextView = findViewById(R.id.usernameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        mobileTextView = findViewById(R.id.mobileTextView);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        editNameTxt = findViewById(R.id.nameEditText);
        editEmailTxt = findViewById(R.id.emailEditText);
        editMobileTxt = findViewById(R.id.mobileEditText);


        db.collection("users")
                .document(uid) // Use the current user's UID as the document ID
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString("name");
                                String mobile = document.getString("mobile");
                                String email = document.getString("email");

                                usernameTextView.setText(name);
                                mobileTextView.setText(mobile);
                                emailTextView.setText(email);

                                editNameTxt.setText(name);
                                editEmailTxt.setText(email);
                                editMobileTxt.setText(mobile);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(UserProfileActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });
        findViewById(R.id.updateProfileButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileImageUri != null) {
                    uploadProfileImage();
                }
                updateUserProfile();

                db.collection("users")
                        .document(uid) // Use the current user's UID as the document ID
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String name = document.getString("name");
                                        String mobile = document.getString("mobile");
                                        String email = document.getString("email");

                                        usernameTextView.setText(name);
                                        mobileTextView.setText(mobile);
                                        emailTextView.setText(email);

                                        editNameTxt.setText(name);
                                        editEmailTxt.setText(email);
                                        editMobileTxt.setText(mobile);
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });
        getUserInfo();
    }

    private void getUserInfo() {
        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    if (snapshot.hasChild("image")) {
                        String image = snapshot.child("image").getValue().toString();
                        myUrl = image;
                        Picasso.get().load(image).into(profileImage);
                        Log.d(TAG, "onDataChange: " + image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {

            }
        });
    }

    private void updateUserProfile() {
        String uid = mAuth.getCurrentUser().getUid();

        String updatedName = editNameTxt.getText().toString();
        String updatedMobile = editEmailTxt.getText().toString();
        String updatedEmail = editMobileTxt.getText().toString();

        // Create a map to update the user data
        Map<String, Object> updatedUserData = new HashMap<>();
        updatedUserData.put("name", updatedName);
        updatedUserData.put("mobile", updatedMobile);
        updatedUserData.put("email", updatedEmail);

        // Update the user data in Firestore
        db.collection("users").document(uid)
                .update(updatedUserData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w(TAG, "Error updating profile", task.getException());
                            Toast.makeText(UserProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadProfileImage() {
        if (profileImageUri != null) {
            final StorageReference fileRef = storageProfilePicsRef.child(mAuth.getCurrentUser().getUid() + ".jpg");

            uploadTask = fileRef.putFile(profileImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = (Uri) task.getResult();
                        myUrl = downloadUrl.toString();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("image", myUrl);
                        databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);
                    }
                }
            });

        } else {
            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        profileImage.setImageURI(uri);
        profileImageUri = uri;
    }

}
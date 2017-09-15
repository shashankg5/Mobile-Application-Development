package com.homework7;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private User currentUser;
    private EditText email;
    private EditText fullName;
    private EditText gender;
    private ImageView changePic;
    private DatabaseReference mDatabase;
    private StorageReference emailRef;
    private StorageReference storageRef;
    private Uri selectedimg;
    private String photourl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("My Profile");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#69D7CF")));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://messagingapp-d8afd.appspot.com");
        changePic = (ImageView) findViewById(R.id.pic);
        currentUser = (User) getIntent().getExtras().getSerializable("currentUser");
        email = (EditText) findViewById(R.id.vEmailID);
        fullName = (EditText) findViewById(R.id.vFullName);
        fullName.setText(currentUser.getFullName());
        gender = (EditText) findViewById(R.id.vGender);
        if(!currentUser.getGender().equals("")) {
            gender.setText(currentUser.getGender());
        }
        if(!currentUser.getPhotourl().equals("")) {
            Picasso.with(ProfileActivity.this).load(currentUser.getPhotourl()).into(changePic);
        }
        email.setText(currentUser.getEmail());
        findViewById(R.id.editFullName).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullName.setEnabled(true);
            }
        });
        findViewById(R.id.editGender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender.setEnabled(true);
            }
        });
        findViewById(R.id.changePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
                mDatabase.child("fullName").setValue(fullName.getText().toString());
                mDatabase.child("gender").setValue(gender.getText().toString());
                if(!photourl.equals("")) {
                    mDatabase.child("photourl").setValue(photourl);

                }
                finish();
            }
        });
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 786);
        } else {
            //openFilePicker();
            Intent takepicture = new Intent(Intent.ACTION_PICK);
            takepicture.setType("image/*");
            startActivityForResult(takepicture, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 786 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"picture"), 1);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    selectedimg = data.getData();
                    Log.d("selected",selectedimg.toString());
                    emailRef = storageRef.child("images/"+currentUser.getEmail());
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedimg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    if(bitmap!=null)
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

                    byte[] data1 = baos.toByteArray();

                    UploadTask uploadTask = emailRef.putBytes(data1);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(ProfileActivity.this, "oops! couldn't upload picture", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(ProfileActivity.this, "Picture uploaded in Firebase", Toast.LENGTH_SHORT).show();
                            Log.d("hello","in success");
                            photourl = downloadUrl.toString();
                            Picasso.with(ProfileActivity.this).load(photourl).into(changePic);

                        }
                    });
                }
                break;
            case 200:


        }
    }
}

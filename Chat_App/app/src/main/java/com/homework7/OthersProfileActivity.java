package com.homework7;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class OthersProfileActivity extends AppCompatActivity {
    private User otherUser;
    private TextView email;
    private TextView fullName;
    private TextView gender;
    private ImageView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);
        otherUser = (User) getIntent().getExtras().get("otherUser");
        setTitle(otherUser.getFullName()+" profile");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#69D7CF")));
        pic = (ImageView) findViewById(R.id.oPic);
        if(!otherUser.getPhotourl().equals("")) {
            Picasso.with(this).load(otherUser.getPhotourl()).into(pic);
        }
        email = (TextView) findViewById(R.id.oEmail);
        email.setText(otherUser.getEmail());
        fullName = (TextView) findViewById(R.id.oName);
        fullName.setText(otherUser.getFullName());
        gender = (TextView) findViewById(R.id.oGender);
        gender.setText(otherUser.getGender());
        findViewById(R.id.viewAlbum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}

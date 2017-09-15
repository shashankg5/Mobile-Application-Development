package com.homework7;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mRepeatPassword;
    private EditText first;
    private EditText last;
    private DatabaseReference mDatabase;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#69D7CF")));
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                first = (EditText) findViewById(R.id.fname);
                last = (EditText) findViewById(R.id.lname);
                mEmailField = (EditText) findViewById(R.id.signEmail);
                mPasswordField = (EditText) findViewById(R.id.pass1);
                mRepeatPassword = (EditText) findViewById(R.id.pass2);
                Log.d("test",mEmailField.getText().toString()+mPasswordField.getText().toString());
                findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
                if(!mEmailField.getText().toString().equals("") && !mPasswordField.getText().toString().equals("") && !first.getText().toString().equals("") && !last.getText().toString().equals("") ) {
                    if(mPasswordField.getText().toString().equals(mRepeatPassword.getText().toString())) {
                        mAuth.createUserWithEmailAndPassword(mEmailField.getText().toString(), mPasswordField.getText().toString())
                                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Log.d("", "createUserWithEmail:onComplete:" + task.isSuccessful());
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignupActivity.this, "User created successfully",
                                                    Toast.LENGTH_SHORT).show();
                                            FirebaseUser user = task.getResult().getUser();
                                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                            intent.putExtra("uid", user.getUid());
                                            mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                                            mDatabase.child(user.getUid()).child("fullName").setValue(first.getText().toString() + " " + last.getText().toString());
                                            mDatabase.child(user.getUid()).child("email").setValue(mEmailField.getText().toString());
                                            mDatabase.child(user.getUid()).child("gender").setValue("");
                                            mDatabase.child(user.getUid()).child("photourl").setValue("");
                                            // mDatabase.child();
                                            startActivity(intent);
                                        }
                                        // If sign in fails, display a message to the user. If sign in succeeds
                                        // the auth state listener will be notified and logic to handle the
                                        // signed in user can be handled in the listener.
                                        if (!task.isSuccessful()) {
                                            if (task.getException().getMessage().equals("The email address is already in use by another account.")) {
                                                //Log.d(TAG, "This email ID already used by someone else");
                                                Toast.makeText(SignupActivity.this, "The email id is already in use by another account. Please use different email id",
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(SignupActivity.this, "Sign up error",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        // [START_EXCLUDE]
                                        // [END_EXCLUDE]
                                    }
                                });
                        // [END create_user_with_email]
                    } else {
                        Toast.makeText(SignupActivity.this, "Passwords doesn't match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

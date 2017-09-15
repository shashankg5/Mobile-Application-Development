package com.group6_inclass10;

import android.content.Intent;
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

public class SignUpActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mFullNameField;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFullNameField = (EditText) findViewById(R.id.sFullName);
                mEmailField = (EditText) findViewById(R.id.sEmail);
                mPasswordField = (EditText) findViewById(R.id.sPass);
                findViewById(R.id.cancelSignUpButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
                mAuth.createUserWithEmailAndPassword(mEmailField.getText().toString(), mPasswordField.getText().toString())
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("", "createUserWithEmail:onComplete:" + task.isSuccessful());
                                if(task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "User created successfully",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = task.getResult().getUser();
                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                    intent.putExtra("uid", user.getUid());
                                    startActivity(intent);
                                }
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    if(task.getException().getMessage().equals("The email address is already in use by another account.")) {
                                        //Log.d(TAG, "This email ID already used by someone else");
                                        Toast.makeText(SignUpActivity.this, "The email id is already in use by another account. Please use different email id",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Sign up error",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                                // [START_EXCLUDE]
                                // [END_EXCLUDE]
                            }
                        });
                // [END create_user_with_email]
            }
        });

    }
}

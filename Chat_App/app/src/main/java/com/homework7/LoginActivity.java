package com.homework7;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener{

    private EditText mEmailField;
    private EditText mPasswordField;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private SignInButton  googlesignIn;
    private boolean found;
    private DatabaseReference mDatabase;
    private User currentUser;
    private HashMap<String, Object> users;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        pd=new ProgressDialog(this);
        pd.setMessage("Please wait ...");
        setTitle("Messaging App");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#69D7CF")));
        LoginManager.getInstance().logOut();
        found=false;
        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    if (found==true) {
                        //for(String uid:users.keySet()) {
                            //Get details of the user with whom the current user chatted
                            //if (uid.equals(user.getUid())) {

                            //} else {
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                                mDatabase.child(user.getUid()).child("fullName").setValue(user.getDisplayName());
                                mDatabase.child(user.getUid()).child("email").setValue(user.getEmail());
                                mDatabase.child(user.getUid()).child("gender").setValue("");
                                mDatabase.child(user.getUid()).child("photourl").setValue("");
                            //}

                       Log.d("sign", "onAuthStateChanged:signed_in:" + user.getUid() + user.getDisplayName() + user.getEmail() + user.getPhotoUrl());
                    }
                    pd.dismiss();
                    Intent intent = new Intent(LoginActivity.this, InboxActivity.class);
                    intent.putExtra("uid", user.getUid());
                    startActivity(intent);
                } else {
                    // User is signed out
                    //pd.dismiss();
                    Log.d("sign", "onAuthStateChanged:signed_out");
                    found=false;
                }
            }
        };


        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken("68566089129-3aevnr98ptvv06ssfos4733clmb6tik6.apps.googleusercontent.com")
        .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        googlesignIn = (SignInButton) findViewById(R.id.google_sign_in_button);
        googlesignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
                pd.show();
            }
        });


        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                mEmailField = (EditText) findViewById(R.id.email);
                mPasswordField = (EditText) findViewById(R.id.pass);
                if(!mEmailField.getText().toString().equals("") && !mPasswordField.getText().toString().equals("")) {
                    mAuth.signInWithEmailAndPassword(mEmailField.getText().toString(), mPasswordField.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("", "signInWithEmail:onComplete:" + task.isSuccessful());

                                    if (task.isSuccessful()) {
                                        FirebaseUser user = task.getResult().getUser();
                                        Intent intent = new Intent(LoginActivity.this, InboxActivity.class);
                                        intent.putExtra("uid", user.getUid());
                                        startActivity(intent);
                                    }
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        pd.dismiss();
                                        Log.w("", "signInWithEmail:failed", task.getException());
                                        Toast.makeText(LoginActivity.this, "Login not successful",
                                                Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }
        });








        //facebook callbackmanager
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("test", "facebook:onSuccess:" + loginResult);
                pd.show();
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });



    }

    void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //handleSignInResult(result);

            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {

            }
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    /*void handleSignInResult(GoogleSignInResult result)
    {
        if(result.isSuccess()){
            GoogleSignInAccount acct=result.getSignInAccount();
        }
        else{}
    }*/

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("test", "handleFacebookAccessToken:" + token);
        found=true;

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("test", "signInWithCredential:onComplete:" + task.isSuccessful());

                        if(task.isSuccessful()){found=true;}
                        if (!task.isSuccessful()) {
                            Log.w("test", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
       // Log.d("test", "firebaseAuthWithGoogle:" + acct.getId());
        found=true;
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("test", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("test", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Google login successful",
                                    Toast.LENGTH_SHORT).show();
                            found=true;

                        }
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}

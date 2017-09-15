package com.homework7;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OtherUsersActivity extends AppCompatActivity {

    private String currentUid;
    private ArrayList<User> usersList;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private UserAdapter userAdapter;
    private DatabaseReference mDatabaseUsers;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_users);
        setTitle("Other users");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#69D7CF")));
        usersList = new ArrayList<>();
        currentUid = getIntent().getExtras().getString("uid");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usersList.clear();
                Log.d("test", "ondatachange for other users");
                HashMap<String, Object> users = (HashMap<String, Object>) dataSnapshot.getValue();
                for(String uid:users.keySet()) {
                    //Dont list current user
                    if(!uid.equals(currentUid)) {
                        User user = new User();
                        user.setUid(uid);
                        HashMap<String, Object> res = (HashMap<String, Object>) users.get(uid);
                        String fullName = (String) res.get("fullName");
                        user.setFullName(fullName);
                        String email = (String) res.get("email");
                        user.setEmail(email);
                        user.setGender((String) res.get("gender"));
                        user.setPhotourl((String) res.get("photourl"));
                        usersList.add(user);
                    } else if(uid.equals(currentUid)) {
                        currentUser = new User();
                        currentUser.setUid(uid);
                        HashMap<String, Object> res = (HashMap<String, Object>) users.get(uid);
                        String fullName = (String) res.get("fullName");
                        currentUser.setFullName(fullName);
                        String email = (String) res.get("email");
                        currentUser.setEmail(email);
                        currentUser.setGender((String) res.get("gender"));
                        currentUser.setPhotourl((String) res.get("photourl"));
                    }

                }
                Log.d("test","other user's list size " +usersList.size());
                Log.d("test", "ondatachange for other users");
                recyclerView = (RecyclerView) findViewById(R.id.rvusers);
                userAdapter = new UserAdapter(usersList, OtherUsersActivity.this);
                layoutManager = new LinearLayoutManager(OtherUsersActivity.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(userAdapter);
                userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
                    @Override
                    public void onInternalItemClick(int position, int id) {
                        if(id==R.id.chatIcon) {
                            Log.d("test", "click internal");
                            Intent intent = new Intent(OtherUsersActivity.this, ChatActivity.class);
                            intent.putExtra("currentuid", currentUid);
                            intent.putExtra("frienduid", usersList.get(position).getUid());
                            intent.putExtra("fullname", usersList.get(position).getFullName());
                            intent.putExtra("photourl", usersList.get(position).getPhotourl());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onRowClick(int position, int id) {
                        Log.d("test", "on row click");
                        Intent intent = new Intent(OtherUsersActivity.this, OthersProfileActivity.class);
                        User otherUser = new User();
                        otherUser = usersList.get(position);
                        intent.putExtra("otherUser", otherUser);
                        startActivity(intent);
                    }

                    @Override
                    public boolean onLongClick(int position, int id) {
                        return false;
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.otherusersmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout1) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(OtherUsersActivity.this, LoginActivity.class);
            startActivity(intent);
        } else  if (item.getItemId() == R.id.inbox) {
            Intent intent = new Intent(OtherUsersActivity.this, InboxActivity.class);
            intent.putExtra("uid", currentUid);
            startActivity(intent);
        } else  if (item.getItemId() == R.id.myprofile1) {
            Intent intent = new Intent(OtherUsersActivity.this, ProfileActivity.class);
            intent.putExtra("currentUser", currentUser);
            startActivity(intent);
        }
        else  if (item.getItemId() == R.id.myalbum1) {
            Intent intent = new Intent(OtherUsersActivity.this, AlbumActivity.class);
            intent.putExtra("currentUser", currentUser);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

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
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;

public class InboxActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String uid;
    private ArrayList<String> chattedFriendsUids;
    private ArrayList<Message> messages;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private InboxAdapter inboxAdapter;
    private ArrayList<User> usersList;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        setTitle("Inbox");
       // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1ec4dc")));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#69D7CF")));


        uid = getIntent().getExtras().getString("uid");
        chattedFriendsUids = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("chats").child(uid);
        //to check whether conversationid already exists
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final HashMap<String, String> myConversationIds = (HashMap<String, String>) dataSnapshot.getValue();

                Log.d("test", "checking..");
                if(myConversationIds!=null) {
                    chattedFriendsUids.clear();
                    for (String friendsUid : myConversationIds.keySet()) {
                        chattedFriendsUids.add(friendsUid);
                        Log.d("test", "conversations exists");
                    }
                    Log.d("test", "chatted with "+chattedFriendsUids.size()+" friends");
                    usersList = new ArrayList<>();
                    DatabaseReference mDatabaseUsers1 = FirebaseDatabase.getInstance().getReference().child("users");
                    mDatabaseUsers1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            usersList.clear();
                            Log.d("test", "ondatachange for other users");
                            HashMap<String, Object> users = (HashMap<String, Object>) dataSnapshot.getValue();
                            for(String uid:users.keySet()) {
                                //Get details of the user with whom the current user chatted
                                if(chattedFriendsUids.contains(uid)) {
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
                                }

                            }
                            recyclerView = (RecyclerView) findViewById(R.id.rv);
                            inboxAdapter = new InboxAdapter(usersList, InboxActivity.this);
                            layoutManager = new LinearLayoutManager(InboxActivity.this, LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(inboxAdapter);
                            inboxAdapter.setOnItemClickListener(new InboxAdapter.OnItemClickListener() {
                                @Override
                                public void onInternalItemClick(int position, int id) {

                                }

                                @Override
                                public void onRowClick(int position, int id) {
                                    Intent intent = new Intent(InboxActivity.this, ChatActivity.class);
                                    intent.putExtra("currentuid", uid);
                                    intent.putExtra("frienduid", usersList.get(position).getUid());
                                    intent.putExtra("fullname", usersList.get(position).getFullName());
                                    intent.putExtra("photourl", usersList.get(position).getPhotourl());
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> users = (HashMap<String, Object>) dataSnapshot.getValue();
                for (String uid1 : users.keySet()) {
                    if (uid1.equals(uid)) {
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
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(InboxActivity.this, LoginActivity.class);
            startActivity(intent);
        } else  if (item.getItemId() == R.id.otherusers) {
            Intent intent = new Intent(InboxActivity.this, OtherUsersActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        } else  if (item.getItemId() == R.id.myprofile) {
            Intent intent = new Intent(InboxActivity.this, ProfileActivity.class);
            intent.putExtra("currentUser", currentUser);
            startActivity(intent);
        } else  if (item.getItemId() == R.id.myalbum) {
            Intent intent = new Intent(InboxActivity.this, AlbumActivity.class);
            intent.putExtra("currentUser", currentUser);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}


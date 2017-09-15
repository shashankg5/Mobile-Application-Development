package com.homework7;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    private String frienduid;
    private String currentuid;
    private String fullName;
    private String photourl;
    private DatabaseReference mDatabase;
    private String converstaionId = "";
    private ArrayList<Message> messages;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ChatAdapter chatAdapter;
    private ArrayList<String> unreadMsgsId;
    private StorageReference emailRef;
    private StorageReference storageRef;
    private Uri selectedimg;
    private ImageView friendProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#69D7CF")));
        unreadMsgsId = new ArrayList<>();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://messagingapp-d8afd.appspot.com");

        frienduid = getIntent().getExtras().getString("frienduid");
        currentuid = getIntent().getExtras().getString("currentuid");
        fullName = getIntent().getExtras().getString("fullname");
        setTitle("Chat with "+fullName);
        photourl = getIntent().getExtras().getString("photourl");
        friendProfilePic = (ImageView) findViewById(R.id.photoIcon);
        if(!photourl.equals("")) {
            Picasso.with(this).load(photourl).into(friendProfilePic);
        }
        TextView name = (TextView) findViewById(R.id.name);
        name.setText(fullName);
        findViewById(R.id.addPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("chats").child(currentuid);
        //to check whether conversationid already exists
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final HashMap<String, Object> myConversationIds = (HashMap<String, Object>) dataSnapshot.getValue();

                Log.d("test", "checking..");
                if(myConversationIds!=null) {
                    for (String index : myConversationIds.keySet()) {
                        if(index.equals(frienduid)) {//conversation exists
                            converstaionId = (String) myConversationIds.get(frienduid);
                            Log.d("test", "conversation with this friend exists");
                            final DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("conversations").child(converstaionId);
                            messages = new ArrayList<Message>();
                            mDatabase2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    messages.clear();
                                    HashMap<String, Object> myConversations = (HashMap<String, Object>) dataSnapshot.getValue();
                                    if(myConversations!=null) {
                                        for (String messageKey : myConversations.keySet()) {
                                            HashMap<String, String> map = (HashMap<String, String>) myConversations.get(messageKey);
                                            Message message = new Message();
                                            message.setText(map.get("text"));
                                            //Log.d("test",message.getText());
                                            // String time =
                                            message.setTime(map.get("time"));
                                            message.setUid(map.get("uid"));
                                            message.setFlag(map.get("flag"));
                                            message.setImgurl(map.get("imgurl"));
                                            message.setKey(map.get("key"));
                                            message.setDeleted(map.get("deleted"));
                                            if (message.getUid().equals(frienduid) && message.getFlag().equals("unread")) {
                                                unreadMsgsId.add(messageKey);
                                            }
                                            if(message.getDeleted().equals("both")||message.getDeleted().equals(currentuid)) {

                                            } else {
                                                messages.add(message);
                                            }

                                        }
                                        Collections.sort(messages, new Comparator<Message>() {
                                            SimpleDateFormat f = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

                                            @Override
                                            public int compare(Message message, Message t1) {
                                                try {
                                                    return f.parse(message.getTime()).compareTo(f.parse(t1.getTime()));
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                return message.getTime().compareTo(t1.getTime());
                                            }
                                        });

                                        int i;
                                        for (i = messages.size() - 1; i >= 0; i--) {
                                            if (messages.get(i).getFlag().equals("read")) {
                                                Log.d("test3", messages.get(i).getText());
                                                break;
                                            }
                                        }

                                        //if accessing from a differnt userid
                                        if (messages.size()!=0 && !messages.get(messages.size() - 1).getUid().equals(currentuid) && i != messages.size() - 1) {
                                            Message  m1 = new Message();
                                            m1.setText("Unread Messages");
                                            m1.setUid("");
                                            messages.add(i + 1, m1);
                                            Log.d("test3", String.valueOf(i + 1));
                                        }

                                        recyclerView = (RecyclerView) findViewById(R.id.rv);
                                        chatAdapter = new ChatAdapter(messages, ChatActivity.this, currentuid);
                                        layoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false);
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setAdapter(chatAdapter);
                                        recyclerView.scrollToPosition(messages.size() - 1);
                                        chatAdapter.setOnItemClickListener(new ChatAdapter.OnItemClickListener() {
                                            @Override
                                            public void onInternalItemClick(int position, int id) {

                                            }

                                            @Override
                                            public void onRowClick(int position, int id) {

                                            }

                                            @Override
                                            public boolean onLongClick(int position, int id) {
                                                Message message = messages.get(position);
                                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("conversations").child(converstaionId);
                                               // mDatabase.child(message.getKey()).removeValue();
                                                if(message.getDeleted().equals("")) {
                                                    mDatabase.child(message.getKey()).child("deleted").setValue(currentuid);
                                                } else  {
                                                    mDatabase.child(message.getKey()).child("deleted").setValue("both");
                                                }

                                                if(messages.size()==1) {
                                                    //messages.remove(0);
                                                    chatAdapter.notifyDataSetChanged();
                                                    /*DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("chats").child(currentuid);
                                                    mDatabase1.child(frienduid).removeValue();
                                                    DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("chats").child(frienduid);
                                                    mDatabase2.child(currentuid).removeValue();*/
                                                }else {
                                                   // messages.remove(position);
                                                }
                                                //chatAdapter.notifyDataSetChanged();


                                                return false;
                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                    }
                    Log.d("test", "checking..");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        findViewById(R.id.addMessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText enter = (EditText) findViewById(R.id.enter);
                if(converstaionId.equals("")) {
                    String conversationKey = mDatabase.push().getKey();
                    converstaionId = conversationKey;
                    mDatabase.child(frienduid).setValue(converstaionId);
                }
                DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("conversations").child(converstaionId);
                String messageKey = mDatabase1.push().getKey();
                Message message = new Message();
                message.setKey(messageKey);
                message.setText(enter.getText().toString());
                message.setImgurl("");
                enter.setText("");
                message.setUid(currentuid);
                message.setFlag("unread");
                Date date = new Date();
                message.setTime(DateFormat.getDateTimeInstance().format(new Date()));
                message.setDeleted("");
                mDatabase1.child(messageKey).setValue(message);
                //Adding the same conversation id for other user to access the same chat thread
                DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("chats");
                mDatabase2.child(frienduid).child(currentuid).setValue(converstaionId);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.d("test4","Back");

        frienduid = getIntent().getExtras().getString("frienduid");
        currentuid = getIntent().getExtras().getString("currentuid");
        fullName = getIntent().getExtras().getString("fullname");
        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("conversations").child(converstaionId);
        for(String messageKey:unreadMsgsId) {
            mDatabase2.child(messageKey).child("flag").setValue("read");
        }
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
                    emailRef = storageRef.child("images/");
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
                            Toast.makeText(ChatActivity.this, "oops! couldn't upload picture", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(ChatActivity.this, "Picture uploaded in Firebase", Toast.LENGTH_SHORT).show();
                            if(converstaionId.equals("")) {
                                String conversationKey = mDatabase.push().getKey();
                                converstaionId = conversationKey;
                                mDatabase.child(frienduid).setValue(converstaionId);
                            }
                            DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("conversations").child(converstaionId);
                            String messageKey = mDatabase1.push().getKey();
                            Message message = new Message();
                            message.setKey(messageKey);
                            message.setImgurl(downloadUrl.toString());
                            Date date = new Date();
                            message.setTime(DateFormat.getDateTimeInstance().format(new Date()));
                            message.setText("");
                            message.setUid(currentuid);
                            message.setFlag("unread");
                            message.setDeleted("");
                            mDatabase1.child(messageKey).setValue(message);
                            //Adding the same conversation id for other user to access the same chat thread
                            DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("chats");
                            mDatabase2.child(frienduid).child(currentuid).setValue(converstaionId);

                            Log.d("hello","in success");
                        }
                    });
                    //imgupload.destroyDrawingCache();
                }
                break;
            case 200:


        }
    }
}
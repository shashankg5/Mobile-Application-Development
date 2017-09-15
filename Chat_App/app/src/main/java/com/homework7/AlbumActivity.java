package com.homework7;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class AlbumActivity extends AppCompatActivity {

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        setTitle("My Album");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#69D7CF")));
        currentUser = (User) getIntent().getExtras().getSerializable("currentUser");
        GridView gridView = (GridView) findViewById(R.id.gridView);
        GridViewAdapter gridViewAdapter = new GridViewAdapter(this, R.layout.album_row, currentUser.getAlbumUrls());
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                /*Intent intent = new Intent(GalleryActivity.this, DetailsActivity.class);
                intent.putExtra("photo", photos.get(position));
                startActivity(intent);*/
            }
        });
    }
}

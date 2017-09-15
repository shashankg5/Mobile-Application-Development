package com.group6_hw06;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText city;
    EditText state;
    TextView textMessage;
    DatabaseDataManager dm;
    List<City> cities;
    SavedListAdapter savedListAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Weather App");
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city = (EditText) findViewById(R.id.city);
                state = (EditText) findViewById(R.id.state);
                String cityS = city.getText().toString().trim();
                String stateS = state.getText().toString().trim();
                if(cityS.equals("")|| cityS==null) {
                    Toast.makeText(MainActivity.this, "Please enter a city", Toast.LENGTH_SHORT).show();
                } else if(stateS.trim().equals("")|| stateS==null) {
                    Toast.makeText(MainActivity.this, "Please enter a state", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, CityWeatherActivity.class);
                    intent.putExtra("city", cityS);
                    intent.putExtra("state", stateS);
                    startActivity(intent);
                }
            }
        });



        dm = new DatabaseDataManager(this);
        //db.execSQL("DROP TABLE IF EXISTS city");
        //dm.saveNote(new City("London", "UK", "22", "true"));
       // dm.saveNote(new City("Charlotte", "US", "15", "true"));
        cities = dm.getAllNotes();
        Log.d("cities size", Integer.toString(cities.size()));
        if(cities.size()!=0) {
            TextView textMessage = (TextView) findViewById(R.id.textMessage);
            textMessage.setVisibility(View.INVISIBLE);
        }
        recyclerView = (RecyclerView) findViewById(R.id.savedrv);
        savedListAdapter = new SavedListAdapter(this, cities);
        recyclerView.setAdapter(savedListAdapter);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        savedListAdapter.setOnItemClickListener(new SavedListAdapter.OnItemClickListener() {
            @Override
            public void onInternaItemClick( int position, int id) {
                if(id==R.id.imageView) {
                    if(cities.get(position).getFavorite().equals("false")) {
                       // Toast.makeText(MainActivity.this, position + " image was clicked!", Toast.LENGTH_SHORT).show();
                        dm.updateNoteFavorite(cities.get(position).getCityName(), "true");
                        cities.get(position).setFavorite("true");
                        cities = dm.getAllNotes();
                        savedListAdapter = new SavedListAdapter(MainActivity.this, cities);
                        recyclerView.setAdapter(savedListAdapter);
                        layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                    /*Collections.swap(cities, position, 0);
                    savedListAdapter.notifyItemMoved(position, 0);*/
                    } else {
                        dm.updateNoteFavorite(cities.get(position).getCityName(), "false");
                        cities.get(position).setFavorite("false");
                        cities = dm.getAllNotes();
                        savedListAdapter = new SavedListAdapter(MainActivity.this, cities);
                        recyclerView.setAdapter(savedListAdapter);
                        layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                    }

                }

            }
        });


        Log.d("demo", cities.toString());

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("on item click", "here");
                Log.d("position", Integer.toString(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Log.d("on item long click", "here");
                Log.d("position", Integer.toString(position));
                Log.d("cities size", String.valueOf(cities.size()));
                boolean deleted = dm.deleteNote(cities.get(position));
                Log.d("deleted", String.valueOf(deleted));
                cities.remove(position);
                savedListAdapter.notifyDataSetChanged();
                savedListAdapter.notifyItemRemoved(position);

            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuButton) {

            Intent intent = new Intent();
            intent.setClassName(this, "com.group6_hw06.MyPreferenceActivity");
            startActivity(intent);
            savedListAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        cities = dm.getAllNotes();
        savedListAdapter = new SavedListAdapter(this, cities);
        recyclerView.setAdapter(savedListAdapter);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        Log.d("on resume", cities.toString());
        SharedPreferences myPreference= PreferenceManager.getDefaultSharedPreferences(this);
        String degree = myPreference.getString("degree", "C");
        Log.d("degree", degree);

    }
}

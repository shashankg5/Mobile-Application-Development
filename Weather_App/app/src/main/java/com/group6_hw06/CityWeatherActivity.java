package com.group6_hw06;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.text.WordUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CityWeatherActivity extends AppCompatActivity implements CityWeatherInterface{

    int onResumeCalled = 0;
    StringBuilder url = new StringBuilder();
    double dbAvg;
    String state;
    String city;
    DatabaseDataManager dm;
    LinearLayoutManager layoutManager1;
    RecyclerView recyclerView2;
    SingleDayAdapter singleDayAdapter;
    ArrayList<Weather> firstDayList;
    ArrayList<Weather> weatherListCopy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d("on create", "here");
        dbAvg=0;
        setContentView(R.layout.activity_city_weather);
        setTitle("City Weather");
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        url.append("http://api.openweathermap.org/data/2.5/forecast?q=");
        state = getIntent().getExtras().get("state").toString();
        city = getIntent().getExtras().get("city").toString();
        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText("Current Location: "+WordUtils.capitalizeFully(city)+", "+state.toUpperCase().trim());
        city  = city.replaceAll(" ", "_");
        url.append(city).append(",").append(state).append("&mode=json&appid=dda0239ad096634c7f7041ebe2f76e87");
        Log.d("url", url.toString());

        new WeatherAsyncTask(progressDialog, this, this).execute(url.toString());

    }

    @Override
    public void setListView(final ArrayList<Weather> weatherList, boolean flag) {
        weatherListCopy = weatherList;
        final ArrayList<Weather> weatherDayList = new ArrayList<>();
        firstDayList = new ArrayList<>();
        final ArrayList<Weather> clickDayList = new ArrayList<>();
        RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.rv1);
        recyclerView2 = (RecyclerView) findViewById(R.id.rv2);


        //setting dayList
        double sumTemp;
        double avgTemp;
        int count;
        int i=0;
        String date;
        int icon;
        Log.d("test",String.valueOf(weatherList.size()));
        while(i<weatherList.size())
        {
            Log.d("test before",String.valueOf(i));
            date=weatherList.get(i).getDate();
            sumTemp=0;
            count=0;
            while ( i<weatherList.size() && date.substring(0,11).trim().equals(weatherList.get(i).getDate().substring(0,11).trim())) {
                Log.d("test before",date.substring(0,11).trim());
                //Log.d("test before",weatherList.get(i).getDate().trim());
                sumTemp=sumTemp+Double.parseDouble(weatherList.get(i).getTemperature());
                count=count+1;
                i=i+1;
                Log.d("test inside while",String.valueOf(i));
            }
            icon=(i-count)+((count/2));
            Weather w1 = new Weather();
            w1.setDate(date);
            avgTemp=sumTemp/count;
            w1.setTemperature(String.format("%.2f", avgTemp));
            Double temp = (avgTemp-32)*5/9;
            w1.setTemperatureInC(String.format("%.2f", temp));
            if(dbAvg==0)
                dbAvg=Double.parseDouble(w1.getTemperatureInC());
           // w1.setTemperatureInC(weatherList.get(i).getTemperatureInC());
            w1.setIconUrl(weatherList.get(icon).getIconUrl());
            weatherDayList.add(w1);
            Log.d("test outside while",String.valueOf(count));
            Log.d("test outside while",String.valueOf(i));
            Log.d("test icon",String.valueOf(icon));
        }

        WeatherAdapter weatherAdapter = new WeatherAdapter(this, weatherDayList);
        recyclerView1.setAdapter(weatherAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager);

        //recyclerView1.setScrollbarFadingEnabled(true);


        //setting first day list on creation
        final String firstDay=weatherList.get(0).getDate().substring(0,11);
        //String sub = weather.getDate().substring(0,11);
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat f2 = new SimpleDateFormat("MMM dd, yyyy");
        try {
            Date date1= f1.parse(firstDay);
            final TextView tv1 = (TextView) findViewById(R.id.midTitle);
            tv1.setText("Three hourly forecast on "+f2.format(date1));
            //textView1.setText(f2.format(date1));
        } catch (ParseException e) {
            e.printStackTrace();
        }




        int j=0;
        while ( j<weatherList.size() && firstDay.substring(0,11).trim().equals(weatherList.get(j).getDate().substring(0,11).trim())) {
            //Log.d("test before",date.substring(0,11).trim());
            //Log.d("test before",weatherList.get(i).getDate().trim());
            /*Weather w2 = new Weather();
            w2.setDate(weatherList.get(j).getDate());
            w2.setTemperature(weatherList.get(j).getTemperature());*/
            firstDayList.add(weatherList.get(j));
            j=j+1;
        }
        singleDayAdapter=new SingleDayAdapter(this, firstDayList);
        recyclerView2.setAdapter(singleDayAdapter);
        layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager1);

        //setting onClickListener
        recyclerView1.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView2, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("click","click");
                        String date= weatherDayList.get(position).getDate().substring(0,11);

                        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat f2 = new SimpleDateFormat("MMM dd, yyyy");
                        try {
                            Date date1= f1.parse(date);
                            final TextView tv1 = (TextView) findViewById(R.id.midTitle);
                            tv1.setText("Three hourly forecast on "+f2.format(date1));
                            //textView1.setText(f2.format(date1));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //tv1.setText("Three hourly forecast on "+date.substring(0,11));
                        int k=0;
                        firstDayList.clear();
                        Log.d("click",String.valueOf(firstDayList.size()));
                        while(k<weatherList.size())
                        {
                            if(date.substring(0,11).trim().equals(weatherList.get(k).getDate().substring(0,11).trim())){
                                while(k<weatherList.size() && date.substring(0,11).trim().equals(weatherList.get(k).getDate().substring(0,11).trim()))
                                {
                                    Log.d("k",String.valueOf(k));
                                    firstDayList.add(weatherList.get(k));
                                    k++;
                                }
                                break;

                            }
                            else{
                                k++;
                            }
                        }

                        //setting to first element
                        layoutManager1.scrollToPositionWithOffset(0, 0);

                        Log.d("click",String.valueOf(firstDayList.size()));
                        Log.d("click","Out of while");

                        singleDayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                })
        );


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menusecond, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.SaveMenuButton) {
            dm = new DatabaseDataManager(this);
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
            City cityObj = new City(WordUtils.capitalizeFully(city).trim(), state.toUpperCase().trim(), Double.toString(dbAvg), "false", dateFormat.format(date));
            boolean updated = dm.updateNoteTemperature(cityObj.getCityName(), cityObj.getTemperature(), cityObj.getDate());
            if(!updated) {
                dm.saveNote(cityObj);
                Toast.makeText(this, "City Saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "City Updated", Toast.LENGTH_SHORT).show();
            }
        } else  if(item.getItemId() == R.id.SettingsMenuButton) {
            Intent intent = new Intent();
            intent.setClassName(this, "com.group6_hw06.MyPreferenceActivity");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("on resume", "here");
        onResumeCalled++;
        if(onResumeCalled==1) {
            Log.d("do nothing", "here");
        } else {
            setListView(weatherListCopy, true);
        }

    }
}

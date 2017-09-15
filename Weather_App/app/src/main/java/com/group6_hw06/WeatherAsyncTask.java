package com.group6_hw06;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by shruti on 10/15/2016.
 */
public class WeatherAsyncTask extends AsyncTask<String, Void, ArrayList<Weather>> {

    ProgressDialog progressDialog;
    CityWeatherInterface cityWeatherInterface;
    Context context;
    private Handler mHandler = new Handler();
    public WeatherAsyncTask(ProgressDialog progressDialog, CityWeatherInterface cityWeatherInterface, Context context)
    {
        this.progressDialog = progressDialog;
        this.cityWeatherInterface = cityWeatherInterface;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Loading Hourly Data");
        progressDialog.show();
    }

    @Override
    protected ArrayList<Weather> doInBackground(String... params) {
        HttpURLConnection con;
        BufferedReader reader = null;
        try {
            URL url = new URL(params[0]);
            con = (HttpURLConnection) url.openConnection();
            con.connect();
            int statusCode = con.getResponseCode();
            con.setRequestMethod("GET");
            if(statusCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = reader.readLine();
                while (line!= null) {
                    sb.append(line);
                    line = reader.readLine();
                }
                return WeatherUtil.WeatherJSONParser.parseWeather(sb.toString());

            } else {
                Toast.makeText( context, "Error encountered: HTTP Status "+statusCode, Toast.LENGTH_SHORT).show();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Weather> weathers) {
        progressDialog.dismiss();
        if(weathers!= null && weathers.size()!=0) {
            cityWeatherInterface.setListView(weathers, false);
        } else {
            //cityWeatherInterface.setListView(weathers, true);

        }

    }
}


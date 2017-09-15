package com.group6_hw06;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by shruti on 10/15/2016.
 */
public class WeatherAdapter extends
        RecyclerView.Adapter<WeatherAdapter.ViewHolder>  {
    private List<Weather> weatherList;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public WeatherAdapter(Context context, List<Weather> weatherList) {
        this.weatherList = weatherList;
        this.mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView dayTemp;
        public ImageView iV;
        public TextView dateDis;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            dayTemp = (TextView) itemView.findViewById(R.id.dayTemp);
            iV=(ImageView) itemView.findViewById(R.id.imageView);
            dateDis = (TextView) itemView.findViewById(R.id.dateDis);
        }
    }
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View day = inflater.inflate(R.layout.day_row, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(day);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Weather weather = weatherList.get(position);
        TextView textView = holder.dayTemp;
        SharedPreferences myPreference= PreferenceManager.getDefaultSharedPreferences(mContext);
        String degree = myPreference.getString("degree", "C");
        if(degree.equals("C")) {
            textView.setText(weather.getTemperatureInC() + (char) 0x00B0 + "C");
        } else {
            textView.setText(weather.getTemperature() + (char) 0x00B0 + "F");
        }
       // textView.setText(weather.getTemperatureInC());
        ImageView imageView = holder.iV;
        Picasso.with(mContext).load(weather.getIconUrl()).into(imageView);
        TextView textView1 = holder.dateDis;
        String sub = weather.getDate().substring(0,11);
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat f2 = new SimpleDateFormat("MMM dd, yyyy");
        try {
            Date date1= f1.parse(sub);
            textView1.setText(f2.format(date1));
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

}

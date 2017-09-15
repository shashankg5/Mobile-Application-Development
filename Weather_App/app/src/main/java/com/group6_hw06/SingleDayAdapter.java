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

import java.util.List;

/**
 * Created by shashank on 17-10-2016.
 */
public class SingleDayAdapter extends
        RecyclerView.Adapter<SingleDayAdapter.ViewHolder>  {
    private List<Weather> weatherList;
    private Context mContext;


    public SingleDayAdapter(Context context, List<Weather> weatherList) {
        this.weatherList = weatherList;
        this.mContext = context;
    }


    private Context getContext() {
        return mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView temp;
        public ImageView imageView;
        public TextView time, condition, pressure, humidity, wind;


        public ViewHolder(View itemView) {
            super(itemView);
            temp = (TextView) itemView.findViewById(R.id.sTempValue);
            imageView=(ImageView) itemView.findViewById(R.id.singleImage);
            time = (TextView) itemView.findViewById(R.id.singleTime);
            condition = (TextView) itemView.findViewById(R.id.sConditionValue);
            pressure=(TextView) itemView.findViewById(R.id.sPressureValue);
            humidity = (TextView) itemView.findViewById(R.id.sHumidityValue);
            wind = (TextView) itemView.findViewById(R.id.sWindValue);

        }
    }
    @Override
    public SingleDayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View day1 = inflater.inflate(R.layout.single_days, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder1 = new ViewHolder(day1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Weather weather = weatherList.get(position);
        String tZone;
        TextView textView = holder.temp;
        ImageView imageView = holder.imageView;
        Picasso.with(mContext).load(weather.getIconUrl()).into(imageView);
        String date = weather.getDate();
        TextView time = holder.time;
        int tInt=Integer.parseInt(date.substring(11, 13));
        if( tInt>11) {
            if( tInt>12){
            tInt = tInt - 12;}
            tZone=" PM";
        }
        else
            tZone=" AM";
        time.setText(String.valueOf(tInt)+date.substring(13, 16)+tZone);
        TextView condition = holder.condition;
        condition.setText(weather.getCondition());
        TextView pressure = holder.pressure;
        pressure.setText(weather.getPressure());
        TextView humidity = holder.humidity;
        humidity.setText(weather.getHumidity());
        TextView wind = holder.wind;
        wind.setText(weather.getWind());
        SharedPreferences myPreference= PreferenceManager.getDefaultSharedPreferences(mContext);
        String degree = myPreference.getString("degree", "C");
        if(degree.equals("C")) {
            textView.setText(weather.getTemperatureInC() + (char) 0x00B0 + "C");
        } else {
            textView.setText(weather.getTemperature() + (char) 0x00B0 + "F");
        }

        //TextView textView1 = holder.timeDis;
        //textView1.setText("paagal");
        /*ImageView imageView = holder.iV;
        Picasso.with(mContext).load(weather.getIconUrl()).into(imageView);
        TextView textView1 = holder.dateDis;
        textView1.setText(weather.getDate());*/

    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

}

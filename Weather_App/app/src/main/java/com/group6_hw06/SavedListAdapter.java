package com.group6_hw06;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shruti on 10/17/2016.
 */
public class SavedListAdapter extends
        RecyclerView.Adapter<SavedListAdapter.ViewHolder>  {
    private List<City> cities;
    // Store the context for easy access
    private Context mContext;
    static private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onInternaItemClick(int position, int id);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Pass in the contact array into the constructor
    public SavedListAdapter(Context context, List<City> cities) {
        this.cities = cities;
        this.mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView loc;
        public TextView temp;
        public TextView time;
        public ImageView star;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            loc = (TextView) itemView.findViewById(R.id.savedLoc);
            temp = (TextView) itemView.findViewById(R.id.savedTemp);
            time = (TextView) itemView.findViewById(R.id.savedTime);
           star = (ImageView) itemView.findViewById(R.id.imageView);
            /*star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (listener != null && position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });*/
        }
    }
    @Override
    public SavedListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View day = inflater.inflate(R.layout.saved_row, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(day);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final City city = cities.get(position);
        TextView loc = holder.loc;
        loc.setText(city.getCityName()+", "+city.getCountry());
        TextView temp = holder.temp;
        SharedPreferences myPreference= PreferenceManager.getDefaultSharedPreferences(mContext);
        String degree = myPreference.getString("degree", "C");
        if(degree.equals("C")) {
            Log.d("saved C",city.getTemperature());
            temp.setText(city.getTemperature() + (char) 0x00B0 + "C");
        } else {
            Double temperature = (Double.parseDouble(city.getTemperature())*9/5)+32;
            Log.d("saved F",String.format("%.2f", temperature));
            temp.setText(String.format("%.2f", temperature) + (char) 0x00B0 + "F");
        }

        TextView time = holder.time;
        time.setText("Updated on: "+city.getDate().substring(0,8));
        holder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(city.getFavorite().equals("false")) {
                    ImageView star = holder.star;
                    star.setImageResource(R.drawable.star_gold);
                    listener.onInternaItemClick(position, view.getId());
                } else {
                    ImageView star = holder.star;
                    star.setImageResource(R.drawable.star_gray);
                    listener.onInternaItemClick(position, view.getId());
                }
            }
        });

        if(city.getFavorite().trim().equals("true")) {
            ImageView star = holder.star;
            star.setImageResource(R.drawable.star_gold);
        }
        //textView2.setText(city.get);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

}

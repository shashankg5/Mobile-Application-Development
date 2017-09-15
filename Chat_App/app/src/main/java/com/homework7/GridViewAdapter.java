package com.homework7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shruti on 10/22/2016.
 */
public class GridViewAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<String> photos;
    int mResource;


    public GridViewAdapter(Context context, int resource, ArrayList<String> photos) {
        this.mContext = context;
        this.photos = photos;
        this.mResource = resource;
    }

    @Override
    public int getCount() {
        return photos.size()+1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.photoImage);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        ImageView image = holder.image;
        // colorName.setText((CharSequence) mData.get(position).colorName);
        //if not last element then load photo, otherwise load addPhoto image
        if(position!=photos.size()) {
            String photo = photos.get(position);
            Picasso.with(mContext).load(photo).into(image);

        } else {
            image.setImageResource(R.drawable.add_photo);
        }
        return convertView;

    }

    static class ViewHolder {
        ImageView image;
    }
}

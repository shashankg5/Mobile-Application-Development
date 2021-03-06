package com.homework7;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by shruti on 10/21/2016.
 */
public class UserAdapter extends
        RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> users;
    private Context mContext;
    static private OnItemClickListener listener;

    public UserAdapter(List<User> users, Context mContext) {
        this.users = users;
        this.mContext = mContext;
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onInternalItemClick(int position, int id);
        void onRowClick(int position, int id);
        boolean onLongClick(int position, int id);
    }


    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View day = inflater.inflate(R.layout.user_row, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(day);
        return viewHolder;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView text;
        public TextView fullname;
        public TextView time;
        public ImageView chatIcon;
        public ImageView profilePic;
        View itemView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder( View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            fullname = (TextView) itemView.findViewById(R.id.userName);
            chatIcon = (ImageView) itemView.findViewById(R.id.chatIcon);
            profilePic = (ImageView) itemView.findViewById(R.id.photo);
           this.itemView = itemView;
        }
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, final int position) {
       User user = users.get(position);
        View itemView = holder.itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("xxxx", "xxxx");
                listener.onRowClick(position, view.getId());
            }
        });
        ImageView chatIcon = holder.chatIcon;
        chatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onInternalItemClick(position, view.getId());
            }
        });
        ImageView profilePic = holder.profilePic;
        if(!user.getPhotourl().equals("")) {
            Picasso.with(mContext).load(user.getPhotourl()).into(profilePic);
        }
        TextView fullname = holder.fullname;
        fullname.setText(user.getFullName());

    }


    @Override
    public int getItemCount() {
        return users.size();
    }
}

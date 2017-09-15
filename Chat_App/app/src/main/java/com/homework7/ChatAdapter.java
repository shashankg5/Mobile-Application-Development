package com.homework7;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by shruti on 11/18/2016.
 */
public class ChatAdapter extends
        RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<Message> messages;
    private Context mContext;
    static private OnItemClickListener listener;
    private String currentuid;

    public ChatAdapter(List<Message> messages, Context mContext, String currentuid) {
        this.messages = messages;
        this.mContext = mContext;
        this.currentuid = currentuid;
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
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View day = inflater.inflate(R.layout.chat_row, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(day);
        return viewHolder;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView text;
        //public TextView fullname;
        public TextView time;
        public ImageView imgMessage;
        View itemView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder( View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.textMessage);
            imgMessage = (ImageView) itemView.findViewById(R.id.imageMessage);
            time = (TextView) itemView.findViewById(R.id.time);
            this.itemView = itemView;
        }
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder holder, final int position) {
        Message message = messages.get(position);
        View itemView = holder.itemView;

        if(message.getText().equals("Unread Messages")) {

            itemView.setPadding(300,0,300,0);
            TextView text = holder.text;
            text.setText(message.getText());
        } else {

            if (message.getUid().equals(currentuid)) {

                itemView.setPadding(350, 0, 0, 0);
            } else {
                itemView.setPadding(0, 0, 350, 0);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("xxxx", "xxxx");
                    listener.onRowClick(position, view.getId());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.d("test", "onLongClick");
                    listener.onLongClick(position, view.getId());
                    return true;
                }
            });
        /*ImageView chatIcon = holder.chatIcon;
        chatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onInternalItemClick(position, view.getId());
            }
        });*/
            if(!message.getText().equals("")) {
                Log.d("test", "this is a text message");
                TextView text = holder.text;
                text.setText(message.getText());
            }
            if(!message.getImgurl().equals("")) {
                Log.d("test", "this is a image message");
                ImageView imageView = holder.imgMessage;
                Picasso.with(mContext).load(message.getImgurl()).into(imageView);
            }
            TextView time = holder.time;
            time.setText(message.getTime());
        }
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }
}

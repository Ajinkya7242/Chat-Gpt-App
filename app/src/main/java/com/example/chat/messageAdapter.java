package com.example.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.myViewHolder> {
    List<Message> messagelist;

    public messageAdapter(List<Message> messagelist) {
        this.messagelist = messagelist;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,null);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Message message=messagelist.get(position);
        if(message.getSendBy().equals(Message.SEND_BY_ME)){
            holder.leftChatView.setVisibility(View.GONE);
            holder.RightChatView.setVisibility(View.VISIBLE);
            holder.txtRigth.setText(message.getMsg());
        }
        else{
            holder.RightChatView.setVisibility(View.GONE);
            holder.leftChatView.setVisibility(View.VISIBLE);
            holder.txtLeft.setText(message.getMsg());
        }

    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftChatView,RightChatView;
        TextView txtLeft,txtRigth;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatView=itemView.findViewById(R.id.leftChatView);
            RightChatView=itemView.findViewById(R.id.rigthChatView);
            txtLeft=itemView.findViewById(R.id.txtleftChat);
            txtRigth=itemView.findViewById(R.id.txtRightChat);
        }
    }
}

package com.example.namedropper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private List<Friend> friendList;
    private MainActivity mainActivity;

    public FriendAdapter(List<Friend> friendList, MainActivity mainActivity) {
        this.friendList = friendList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend friend = friendList.get(position);
        holder.nameTextView.setText(friend.getName());
        holder.phoneTextView.setText(friend.getPhoneNumber());
        holder.editButton.setOnClickListener(v -> mainActivity.editFriend(friend));
        holder.removeButton.setOnClickListener(v -> mainActivity.removeFriend(friend));
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public void setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, phoneTextView;
        Button editButton, removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            editButton = itemView.findViewById(R.id.editButton);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}



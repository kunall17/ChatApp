package com.kunall17.chatapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kunall17.chatapp.R;
import com.kunall17.chatapp.pojo.ChatRoom;
import com.kunall17.chatapp.viewholders.ChatRoomHolder;
import com.kunall17.chatapp.viewmodels.ChatRoomsViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomHolder> {

    private final ChatRoomsViewModel dataViewModel;
    private List<ChatRoom> chatRoomList = new ArrayList<>();

    public ChatRoomsAdapter(ChatRoomsViewModel dataViewModel) {
        this.dataViewModel = dataViewModel;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return chatRoomList.size();
    }

    public void setData(List<ChatRoom> newList) {
        this.chatRoomList = newList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatRoomHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_room_item, parent, false), dataViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomHolder holder, int position) {
        holder.set(chatRoomList.get(position));
    }
}

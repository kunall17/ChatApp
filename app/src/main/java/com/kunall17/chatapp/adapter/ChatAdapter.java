package com.kunall17.chatapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kunall17.chatapp.R;
import com.kunall17.chatapp.pojo.Message;
import com.kunall17.chatapp.viewholders.ChatViewHolder;
import com.kunall17.chatapp.viewmodels.ChatViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private final ChatViewModel dataViewModel;
    private List<Message> chatRoomList = new ArrayList<>();

    public ChatAdapter(ChatViewModel dataViewModel) {
        this.dataViewModel = dataViewModel;
    }

    public void setData(List<Message> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new Diff(this.chatRoomList, newList));
        chatRoomList = newList;
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return chatRoomList.size();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false), dataViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.set(chatRoomList.get(position));
    }
}
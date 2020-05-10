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

import static com.kunall17.chatapp.viewmodels.ChatViewModel.VIEW_TYPE_RECIEVED;
import static com.kunall17.chatapp.viewmodels.ChatViewModel.VIEW_TYPE_SELF;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private final ChatViewModel dataViewModel;
    private List<Message> chatRoomList = new ArrayList<>();

    public ChatAdapter(ChatViewModel dataViewModel) {
        this.dataViewModel = dataViewModel;
    }

    public void setData(List<Message> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new Diff(this.chatRoomList, newList));
        for (int i = chatRoomList.size(); i < newList.size(); i++) {
            chatRoomList.add(newList.get(i));
        }
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

    @Override
    public int getItemViewType(int position) {
        return dataViewModel.getItemViewType(position);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_RECIEVED)
            return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_recieved, parent, false), dataViewModel);
        else if (viewType == VIEW_TYPE_SELF)
            return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_sent, parent, false), dataViewModel);
        else
            throw new RuntimeException("invalid");
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.set(chatRoomList.get(position));
    }
}

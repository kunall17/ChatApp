package com.kunall17.chatapp.viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.kunall17.chatapp.R;
import com.kunall17.chatapp.pojo.ChatRoom;
import com.kunall17.chatapp.viewmodels.ChatRoomsViewModel;

public class ChatRoomHolder extends RecyclerView.ViewHolder {

    private final ChatRoomsViewModel viewModel;
    private AppCompatTextView title;

    public ChatRoomHolder(@NonNull View rootView, ChatRoomsViewModel dataViewModel) {
        super(rootView);
        title = rootView.findViewById(R.id.title);
        rootView.setOnClickListener(view -> dataViewModel.onChatRoomItemClick(getAdapterPosition(), rootView.getContext()));
        this.viewModel = dataViewModel;
    }

    public void set(ChatRoom chatRoom) {
        title.setText(chatRoom.getTitle());
    }
}

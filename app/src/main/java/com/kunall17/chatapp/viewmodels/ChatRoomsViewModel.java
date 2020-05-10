package com.kunall17.chatapp.viewmodels;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kunall17.chatapp.pojo.ChatRoom;
import com.kunall17.chatapp.ui.ChatActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomsViewModel extends ViewModel {

    //    private APIRepository APIRepository;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<List<ChatRoom>> chatRoomList = new MutableLiveData<>();

    public ChatRoomsViewModel() {
        isLoading.setValue(false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        init();
    }

    public MutableLiveData<List<ChatRoom>> getChatRoomList() {
        return chatRoomList;
    }

    private void init() {
        isLoading.setValue(true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDb = database.getReference("chatrooms");
        mDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<ChatRoom> rooms = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ChatRoom chatroom = new ChatRoom(child.getKey());
                    rooms.add(chatroom);
                }
                chatRoomList.setValue(rooms);
                isLoading.setValue(false);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                isLoading.setValue(false);
                // Failed to read value
            }
        });
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void onChatRoomItemClick(int adapterPosition, Context context) {
        if (this.getChatRoomList().getValue() != null) {
            ChatRoom chatRoom = this.getChatRoomList().getValue().get(adapterPosition);
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("title", chatRoom.getTitle());
            context.startActivity(intent);
        }
    }
}

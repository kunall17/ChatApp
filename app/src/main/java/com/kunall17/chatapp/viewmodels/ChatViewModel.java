package com.kunall17.chatapp.viewmodels;

import android.text.Editable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kunall17.chatapp.pojo.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<List<Message>> chatRoomList = new MutableLiveData<>();
    private DatabaseReference mDb;

    public ChatViewModel(String title) {
        isLoading.setValue(false);
        init(title);
    }

    public MutableLiveData<List<Message>> getChatRoomList() {
        return chatRoomList;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }


    private void init(String title) {
        isLoading.setValue(true);
        chatRoomList.setValue(new ArrayList<>());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDb = database.getReference("chatrooms/" + title);
        mDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<Message> lists = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Message message = child.getValue(Message.class);
                    message.setTimestamp(Long.valueOf(child.getKey()));
                    lists.add(message);
                }
                chatRoomList.setValue(lists);
                isLoading.setValue(false);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                isLoading.setValue(false);
                // Failed to read value
            }
        });
    }

    public void sendMessage(Editable text) {
        isLoading.setValue(true);
        mDb.child(getNewIdForMessage()).setValue(new Message(text.toString(), "username"))
                .addOnSuccessListener(aVoid -> {
                    isLoading.setValue(false);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    isLoading.setValue(false);
                });
    }

    private String getNewIdForMessage() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    public boolean isSelfMessage(int adapterPosition) {
        if (chatRoomList.getValue() == null)
            throw new RuntimeException("chat room list cannot be null");
        return chatRoomList.getValue().get(adapterPosition).username.equals(username);
    }

    public int getItemViewType(int position) {
        if (isSelfMessage(position))
            return VIEW_TYPE_SELF;
        else return VIEW_TYPE_RECIEVED;
    }
}

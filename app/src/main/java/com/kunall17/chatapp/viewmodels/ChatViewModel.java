package com.kunall17.chatapp.viewmodels;

import android.text.Editable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kunall17.chatapp.pojo.Message;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {

    public static final int VIEW_TYPE_RECIEVED = 1;
    public static final int VIEW_TYPE_SELF = 2;
    private static final Long INITIATED_TIME = System.currentTimeMillis() / 1000;
    private final String username;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<List<Message>> chatRoomList = new MutableLiveData<>();
    private DatabaseReference mDb;

    public ChatViewModel(String title, String username) {
        isLoading.setValue(false);
        init(title);
        this.username = username;
    }

    public MutableLiveData<List<Message>> getChatRoomList() {
        return chatRoomList;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadNewElements(Long key) {
        Log.d("ChatViewModelseehere", "loadNewElements() called with: key = [" + key + "]");
        mDb.orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("ChatViewModelseehere", "onChildAdded() called with: dataSnapshot = [" + dataSnapshot + "], s = [" + s + "]");
                List<Message> rooms = chatRoomList.getValue();
                Message message = dataSnapshot.getValue(Message.class);
                message.setTimestamp(Long.valueOf(dataSnapshot.getKey()));
                rooms.add(message);
                if (!message.username.equals(username) && message.getTimestamp() > INITIATED_TIME) {
                    EventBus.getDefault().post(message);
                }
                chatRoomList.setValue(rooms);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init(String title) {
        isLoading.setValue(true);
        chatRoomList.setValue(new ArrayList<>());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDb = database.getReference("chatrooms/" + title);
        mDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                Long timestamp = lists.get(lists.size() - 1).getTimestamp();
                loadNewElements(timestamp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sendMessage(Editable text) {
        isLoading.setValue(true);
        mDb.child(getNewIdForMessage()).setValue(new Message(text.toString(), username))
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

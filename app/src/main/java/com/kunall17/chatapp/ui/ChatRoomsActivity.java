package com.kunall17.chatapp.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kunall17.chatapp.R;
import com.kunall17.chatapp.adapter.ChatRoomsAdapter;
import com.kunall17.chatapp.viewmodels.ChatRoomsViewModel;

public class ChatRoomsActivity extends AppCompatActivity {

    private RecyclerView chatRv;
    private ProgressDialog progressDialog;
    private ChatRoomsViewModel dataViewModel;

    public void showLoader(boolean b) {
        if (isFinishing()) return;
        if (b) {
            if (progressDialog != null)
                return;
            this.progressDialog = ProgressDialog.show(this, "", "", true, false);
            if (this.progressDialog.getWindow() == null) return;
            this.progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            this.progressDialog.setContentView(R.layout.progressdialog);
        } else if (this.progressDialog != null) {
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        chatRv = findViewById(R.id.recycler_view);
        dataViewModel = new ChatRoomsViewModel(getIntent().getStringExtra("username"));
        ChatRoomsAdapter adapter = new ChatRoomsAdapter(dataViewModel);
        setTitle("Chat rooms");
        adapter.setHasStableIds(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        chatRv.setHasFixedSize(true);
        chatRv.setLayoutManager(layoutManager);
        chatRv.setAdapter(adapter);
        dataViewModel.getChatRoomList().observe(this, adapter::setData);
        dataViewModel.getIsLoading().observe(this, this::showLoader);
    }
}

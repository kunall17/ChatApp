package com.kunall17.chatapp.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kunall17.chatapp.R;
import com.kunall17.chatapp.adapter.ChatAdapter;
import com.kunall17.chatapp.viewmodels.ChatViewModel;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRv;
    private ProgressDialog progressDialog;
    private ChatViewModel dataViewModel;
    private AppCompatEditText viewById;

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
        setContentView(R.layout.activity_chat);

        chatRv = findViewById(R.id.recycler_view);
        viewById = findViewById(R.id.editText);
        ChatAdapter adapter = new ChatAdapter(dataViewModel);
        adapter.setHasStableIds(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        chatRv.setLayoutManager(layoutManager);
        chatRv.setAdapter(adapter);

        String title = getIntent().getStringExtra("title");
        dataViewModel = new ChatViewModel(title);
        dataViewModel.getChatRoomList().observe(this, adapter::setData);
        dataViewModel.getIsLoading().observe(this, this::showLoader);

        findViewById(R.id.button2).setOnClickListener(view -> {
            if (TextUtils.isEmpty(viewById.getText())) {
                Toast.makeText(ChatActivity.this, "Please enter some message", Toast.LENGTH_LONG).show();
            } else {
                dataViewModel.sendMessage(viewById.getText());
            }
        });
    }
}

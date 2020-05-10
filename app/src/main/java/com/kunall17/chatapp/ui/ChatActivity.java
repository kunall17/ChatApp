package com.kunall17.chatapp.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.kunall17.chatapp.R;
import com.kunall17.chatapp.adapter.ChatAdapter;
import com.kunall17.chatapp.pojo.Message;
import com.kunall17.chatapp.viewmodels.ChatViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRv;
    private ProgressDialog progressDialog;
    private ChatViewModel dataViewModel;
    private AppCompatEditText messageEt;

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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    public void displaySnackbar(String str) {
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), str, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snack.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageRecieved(Message event) {
        displaySnackbar("New message recieved from " + event.getUsername());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatRv = findViewById(R.id.recycler_view);
        messageEt = findViewById(R.id.editText);
        String title = getIntent().getStringExtra("title");
        String username = getIntent().getStringExtra("username");
        dataViewModel = new ChatViewModel(title, username);
        ChatAdapter adapter = new ChatAdapter(dataViewModel);
        adapter.setHasStableIds(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        chatRv.setLayoutManager(layoutManager);
        chatRv.setAdapter(adapter);
        setTitle(title);
        chatRv.setItemAnimator(null);
        dataViewModel.getChatRoomList().observe(this, (v) -> {
            adapter.setData(v);
            chatRv.smoothScrollToPosition(v.size());
        });
        dataViewModel.getIsLoading().observe(this, v -> {
            showLoader(v);
            if (!v)
                chatRv.smoothScrollToPosition(adapter.getItemCount());
        });

        findViewById(R.id.sendButton).setOnClickListener(view -> {
            if (TextUtils.isEmpty(messageEt.getText())) {
                Toast.makeText(ChatActivity.this, "Please enter some message", Toast.LENGTH_LONG).show();
            } else {
                dataViewModel.sendMessage(messageEt.getText());

                View t = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(t.getWindowToken(), 0);
                }
                messageEt.setText("");
            }
        });
    }
}

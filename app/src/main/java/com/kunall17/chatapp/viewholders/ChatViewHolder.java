package com.kunall17.chatapp.viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.kunall17.chatapp.R;
import com.kunall17.chatapp.pojo.Message;
import com.kunall17.chatapp.viewmodels.ChatViewModel;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    private final AppCompatTextView message;
    private final AppCompatTextView timestamp;
    private final AppCompatTextView username;
    private final AppCompatTextView userImage;
    private final ChatViewModel viewModel;

    public ChatViewHolder(@NonNull View itemView, ChatViewModel dataViewModel) {
        super(itemView);
        this.message = itemView.findViewById(R.id.message);
        this.username = itemView.findViewById(R.id.username);
        this.timestamp = itemView.findViewById(R.id.timestamp);
        this.userImage = itemView.findViewById(R.id.userImage);
        this.viewModel = dataViewModel;
    }

    public void set(Message message) {
        boolean isSelf = viewModel.isSelfMessage(getAdapterPosition());
        if (!isSelf) {
            userImage.setText(message.getUsername().substring(0, 1));
            this.username.setText(message.getUsername());
        } else {

        }
        this.message.setText(message.getMessage());

        try {
            Date time = new Date(message.getTimestamp() * 1000);
            PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
            String ago = prettyTime.format(time);
            timestamp.setText(ago);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

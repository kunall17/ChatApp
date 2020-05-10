package com.kunall17.chatapp.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kunall17.chatapp.ui.ChatRoomsActivity;

public class LoginViewModel extends AndroidViewModel {

    private final String username;
    private final SharedPreferences sharedPreferences;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);
        isLoading.setValue(false);
        if (!TextUtils.isEmpty(username)) {
            navigateToChat(username);
        }
    }

    public void navigateToChat(String username) {
        Intent intent = new Intent(getApplication(), ChatRoomsActivity.class);
        intent.putExtra("username", username);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }

    public void loginButtonPressed(Editable text) {
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(getApplication(), "Please enter something in username", Toast.LENGTH_SHORT).show();
        } else {
            isLoading.setValue(true);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("username", text.toString());
            edit.commit();
            navigateToChat(text.toString());
            isLoading.setValue(false);
        }
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
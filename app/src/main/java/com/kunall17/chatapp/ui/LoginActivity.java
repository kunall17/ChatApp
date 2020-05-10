package com.kunall17.chatapp.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProvider;

import com.kunall17.chatapp.R;
import com.kunall17.chatapp.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    LoginViewModel loginViewModel;
    private ProgressDialog progressDialog;

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
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        setContentView(R.layout.activity_login);

        final AppCompatEditText username = findViewById(R.id.username);
        findViewById(R.id.button).setOnClickListener(view -> {
            loginViewModel.loginButtonPressed(username.getText());
        });
        loginViewModel.getIsLoading().observe(this, this::showLoader);
    }
}

package com.pramit.rmh.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.pramit.rmh.AWSConnection;
import com.pramit.rmh.Home;
import com.pramit.rmh.R;
import com.pramit.rmh.ui.UIUtils;

public class Login extends AppCompatActivity {

    private AWSConnection aws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSettings();
        this.aws = AWSConnection.getInstance(getApplicationContext());
        if (aws.getCachedIdentityId() != null)
            UIUtils.changeActivity(this, Home.class);
        setContentView(R.layout.activity_login);
        initListeners();
    }

    public void initSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String darkMode = sharedPreferences.getString("darkMode", "");
        switch (darkMode) {
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "battery":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                editor.putString("darkMode", "system");
                editor.apply();
                break;
        }
    }

    public void initListeners() {
        EditText userIdField = findViewById(R.id.username);
        findViewById(R.id.signUpLink).setOnClickListener(e -> {
            Intent newAccount = new Intent(this, CreateAccount.class);
            startActivity(newAccount);
        });
        findViewById(R.id.login).setOnClickListener(e -> {
            String username = userIdField.getText().toString();
            String password = ((EditText) findViewById(R.id.password)).getText().toString();
            aws.userLogin(this, username, password);
        });
        findViewById(R.id.googleAuth).setOnClickListener(e -> {
            //TODO: GOOGLE OAUTH
        });
        findViewById(R.id.facebookAuth).setOnClickListener(e -> {
            //TODO: FACEBOOK OAUTH
        });
        findViewById(R.id.appleAuth).setOnClickListener(e -> {
            //TODO: APPLE OAUTH
        });
        findViewById(R.id.forgotPass).setOnClickListener(e -> {
            String userId = userIdField.getText().toString();
            ForgotAccount forgotAccountDialog = new ForgotAccount(userId, aws);
            forgotAccountDialog.show(getSupportFragmentManager(), ForgotAccount.FRAGMENT_TAG);
        });

        findViewById(R.id.root).getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            //initUI();
        });
    }
    /*public void initUI() {
        View background = findViewById(R.id.root);
        View cardView = findViewById(R.id.card);
        GraphicsUtils.blurUIComponent(background, cardView, 15, 10);
    }*/
}

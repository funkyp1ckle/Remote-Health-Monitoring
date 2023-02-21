package com.pramit.rhm.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.pramit.rhm.R;
import com.pramit.rhm.util.AWSUtils;

public class Login extends AppCompatActivity {
    private AWSUtils aws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.aws = AWSUtils.getInstance(getApplicationContext());
        aws.continueSession(this);
        setContentView(R.layout.activity_login);
        initListeners();
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

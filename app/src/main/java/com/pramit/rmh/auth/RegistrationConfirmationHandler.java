package com.pramit.rmh.auth;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;

public class RegistrationConfirmationHandler implements GenericHandler {
    private final Context context;

    public RegistrationConfirmationHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess() {
        Intent newActivity = new Intent(context, Login.class);
        newActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newActivity);
    }

    @Override
    public void onFailure(Exception exception) {
        Toast.makeText(context, "User registration failed", Toast.LENGTH_LONG).show();
    }
}

package com.pramit.rmh.auth;

import android.content.Context;
import android.widget.Toast;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.pramit.rmh.Home;
import com.pramit.rmh.ui.UIUtils;

public class RegistrationConfirmationHandler implements GenericHandler {
    private final Context context;

    public RegistrationConfirmationHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess() {
        UIUtils.changeActivity(context, Home.class);
    }

    @Override
    public void onFailure(Exception exception) {
        Toast.makeText(context, "User registration failed", Toast.LENGTH_LONG).show();
    }
}

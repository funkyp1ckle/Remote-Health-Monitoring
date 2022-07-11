package com.pramit.rmh.auth;

import android.content.Context;
import android.widget.Toast;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;

public class RegistrationHandler implements SignUpHandler {
    private final Context context;

    public RegistrationHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(CognitoUser user, SignUpResult signUpResult) {
        if (!signUpResult.isUserConfirmed()) {
        }
    }

    @Override
    public void onFailure(Exception exception) {
        Toast.makeText(context, "There was an error creating the account", Toast.LENGTH_LONG).show();
    }
}

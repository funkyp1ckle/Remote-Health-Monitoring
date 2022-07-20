package com.pramit.rmh.auth;

import android.content.Context;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;
import com.pramit.rmh.AWSConnection;

public class RegistrationHandler implements SignUpHandler {
    private final Context context;
    private final AWSConnection aws;

    public RegistrationHandler(Context context) {
        this.context = context;
        this.aws = AWSConnection.getInstance(context.getApplicationContext());
    }

    @Override
    public void onSuccess(CognitoUser user, SignUpResult signUpResult) {
        RegistrationConfirmationOTPInput input = new RegistrationConfirmationOTPInput(user.getUserId(), aws);
        input.show(((AppCompatActivity) context).getSupportFragmentManager(), RegistrationConfirmationOTPInput.FRAGMENT_TAG);
    }

    @Override
    public void onFailure(Exception exception) {
        Toast.makeText(context, "There was an error creating the account", Toast.LENGTH_LONG).show();
    }
}

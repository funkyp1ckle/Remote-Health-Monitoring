package com.pramit.rmh.auth;

import android.content.Context;
import android.widget.Toast;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.pramit.rmh.R;

public class PasswordResetHandler implements ForgotPasswordHandler {
    private final Context context;

    public PasswordResetHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void getResetCode(ForgotPasswordContinuation continuation) {
        continuation.continueTask();
        Toast.makeText(context, R.string.resetInstructions, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(Exception exception) {
    }
}

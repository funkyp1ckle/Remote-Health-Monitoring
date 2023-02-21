package com.pramit.rhm.auth;

import android.content.Context;
import android.widget.Toast;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;

public class PasswordResetConfirmationHandler implements ForgotPasswordHandler {
    private final Context context;
    private final String newPassword;
    private final String otp;

    public PasswordResetConfirmationHandler(Context context, String newPassword, String otp) {
        this.context = context;
        if (newPassword != null)
            this.newPassword = newPassword;
        else
            this.newPassword = "";
        if (otp != null)
            this.otp = otp;
        else
            this.otp = "";
    }

    @Override
    public void onSuccess() {
        Toast.makeText(context, "Password has been reset", Toast.LENGTH_LONG).show();
    }

    @Override
    public void getResetCode(ForgotPasswordContinuation continuation) {
        continuation.setVerificationCode(otp);
        continuation.setPassword(newPassword);

    }

    @Override
    public void onFailure(Exception exception) {
        Toast.makeText(context, "Could not change password", Toast.LENGTH_LONG).show();
    }
}

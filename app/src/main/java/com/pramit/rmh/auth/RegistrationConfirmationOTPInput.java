package com.pramit.rmh.auth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.fragment.app.DialogFragment;
import com.pramit.rmh.R;
import com.pramit.rmh.util.AWSUtils;
import org.jetbrains.annotations.NotNull;

public class RegistrationConfirmationOTPInput extends DialogFragment {
    public static final String FRAGMENT_TAG = "Registration Confirmation OTP Input";
    private final String userId;
    private final AWSUtils aws;

    public RegistrationConfirmationOTPInput(String userId, AWSUtils aws) {
        this.userId = userId;
        this.aws = aws;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.DialogFragment);
        dialogBuilder.setTitle(R.string.acc_verification);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_confirm_acc, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setPositiveButton(R.string.acc_verification_btn, (dialogInterface, i) -> {
            String code = ((EditText) dialogView.findViewById(R.id.otp)).getText().toString();
            aws.confirmUser(getContext(), userId, code);
        });
        return dialogBuilder.create();
    }
}

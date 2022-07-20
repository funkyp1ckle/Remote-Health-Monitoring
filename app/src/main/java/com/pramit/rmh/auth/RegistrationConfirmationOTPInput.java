package com.pramit.rmh.auth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.fragment.app.DialogFragment;
import com.pramit.rmh.AWSConnection;
import com.pramit.rmh.R;
import org.jetbrains.annotations.NotNull;

public class RegistrationConfirmationOTPInput extends DialogFragment {
    public static final String FRAGMENT_TAG = "Registration Confirmation OTP Input";
    private final String userId;
    private final AWSConnection aws;

    public RegistrationConfirmationOTPInput(String userId, AWSConnection aws) {
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
        LinearLayout layout = dialogView.findViewById(R.id.confirm_acc_layout);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setPositiveButton(R.string.acc_verification_btn, (dialogInterface, i) -> {
            String code = ((EditText) dialogView.findViewById(R.id.otp)).getText().toString();
            aws.confirmUser(getContext(), userId, code);
        });
        return dialogBuilder.create();
    }
}

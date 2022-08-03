package com.pramit.rmh.auth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.autofill.HintConstants;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.pramit.rmh.R;
import com.pramit.rmh.util.AWSUtils;
import com.pramit.rmh.util.UIUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class ForgotAccount extends DialogFragment {
    public static final String FRAGMENT_TAG = "forgot_account_dialog";
    private final AWSUtils aws;
    private String userId;

    public ForgotAccount(String userId, AWSUtils aws) {
        this.userId = userId;
        this.aws = aws;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.DialogFragment);
        dialogBuilder.setTitle(R.string.forgotPass);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_forgot_password, null);
        LinearLayout layout = dialogView.findViewById(R.id.forgot_password_layout);
        dialogBuilder.setView(dialogView);

        EditText usernameEditText = dialogView.findViewById(R.id.username);
        usernameEditText.setText(userId);

        Context context = getContext();
        TextInputLayout newPasswordBox = UIUtils.createTextBox(context, R.string.newPasswordInstructions);
        Objects.requireNonNull(newPasswordBox.getEditText()).setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        Objects.requireNonNull(newPasswordBox.getEditText()).setAutofillHints(HintConstants.AUTOFILL_HINT_NEW_PASSWORD);
        newPasswordBox.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        TextInputLayout otpBox = UIUtils.createTextBox(context, R.string.otpInstructions);
        Objects.requireNonNull(otpBox.getEditText()).setAutofillHints(HintConstants.AUTOFILL_HINT_SMS_OTP, HintConstants.AUTOFILL_HINT_EMAIL_OTP, HintConstants.AUTOFILL_HINT_2FA_APP_OTP);

        dialogBuilder.setPositiveButton(R.string.getOTP, null);
        AlertDialog dialog = dialogBuilder.create();
        dialog.setOnShowListener(dialogInterface -> {
            AtomicInteger clickCounter = new AtomicInteger(0);
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                userId = usernameEditText.getText().toString();
                int curCounterVal = clickCounter.get();
                if (curCounterVal == 0 && !userId.equals("")) {
                    aws.forgotPassword(context, userId);
                    layout.addView(newPasswordBox);
                    layout.addView(otpBox);
                    button.setText(R.string.resetPass);
                    clickCounter.getAndIncrement();
                } else if (curCounterVal == 1 && !(newPasswordBox.getEditText().getText().toString().equals("") || (otpBox.getEditText().getText().toString().equals("")))) {
                    String password = newPasswordBox.getEditText().getText().toString();
                    String otpCode = otpBox.getEditText().getText().toString();
                    aws.confirmForgotPassword(context, userId, password, otpCode);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Fill in all fields", Toast.LENGTH_LONG).show();
                }
            });
        });
        return dialog;
    }
}

package com.pramit.rmh.ui;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pramit.rmh.R;

import static com.google.android.material.datepicker.MaterialDatePicker.todayInUtcMilliseconds;

public class UIUtils {
    public static TextInputLayout GetTextBox(Context context, int hintResourceId) {
        TextInputLayout inputLayout = new TextInputLayout(context);
        LinearLayout.LayoutParams textInputLayoutParams = new LinearLayout.LayoutParams(GraphicsUtils.dpToPx(context, 280), GraphicsUtils.dpToPx(context, 48));

        int topMargin = GraphicsUtils.dpToPx(context, 10);
        int horizontalMargin = GraphicsUtils.dpToPx(context, 20);
        textInputLayoutParams.setMargins(horizontalMargin, topMargin, horizontalMargin, 0);
        inputLayout.setLayoutParams(textInputLayoutParams);
        int boxCornerRadius = GraphicsUtils.dpToPx(context, 20);
        inputLayout.setBoxBackgroundColorResource(R.color.editTextBackground);
        inputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_FILLED);
        inputLayout.setBoxCornerRadii(boxCornerRadius, boxCornerRadius, boxCornerRadius, boxCornerRadius);
        inputLayout.setBoxStrokeWidth(0);
        inputLayout.setBoxStrokeWidthFocused(0);
        inputLayout.setHintEnabled(false);

        TextInputEditText editText = new TextInputEditText(inputLayout.getContext());
        ViewGroup.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(editTextParams);
        int horizontalPadding = GraphicsUtils.dpToPx(inputLayout.getContext(), 15);
        editText.setPadding(horizontalPadding, 0, horizontalPadding, 0);
        editText.setHint(context.getResources().getText(hintResourceId));

        inputLayout.addView(editText, editTextParams);
        return inputLayout;
    }

    public static MaterialDatePicker.Builder<Long> createDateDialog(Context context, int titleId) {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText(context.getResources().getText(titleId));
        builder.setSelection(todayInUtcMilliseconds());
        return builder;
    }
}

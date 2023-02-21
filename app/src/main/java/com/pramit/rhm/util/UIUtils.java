package com.pramit.rhm.util;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pramit.rhm.R;
import com.pramit.rhm.device.Device;

import static com.google.android.material.datepicker.MaterialDatePicker.todayInUtcMilliseconds;

public class UIUtils {
    private static final float BITMAP_SCALE = 0.5f;

    static {
        System.loadLibrary("GraphicsUtils");
    }

    //FIX SCALING
    public static void blurUIComponent(View background, View uiComponent, int blurRadius, int cornerRadius) {
        Bitmap backgroundBitmap = getBackgroundBitmapFromView(background);
        Bitmap blurredBackgroundBitmap = blur(backgroundBitmap, blurRadius);
        Bitmap uiComponentBitmap = getBackgroundBitmapFromView(uiComponent);
        Bitmap mergedBitmap = merge(blurredBackgroundBitmap, uiComponentBitmap, (int) (uiComponent.getX() * BITMAP_SCALE), (int) (uiComponent.getY() * BITMAP_SCALE));
        RoundedBitmapDrawable roundedBitmap = RoundedBitmapDrawableFactory.create(uiComponent.getResources(), mergedBitmap);
        roundedBitmap.setCornerRadius(cornerRadius);
        setComponentBackground(uiComponent, roundedBitmap);
    }

    private static void setComponentBackground(View uiComponent, RoundedBitmapDrawable roundedBitmap) {
        Canvas canvas = new Canvas(Bitmap.createBitmap(uiComponent.getWidth(), uiComponent.getHeight(), Bitmap.Config.ARGB_8888));
        float scale = 1 / BITMAP_SCALE;
        canvas.scale(scale, scale);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(roundedBitmap.getBitmap(), 0, 0, paint);
    }

    public static Bitmap getBackgroundBitmapFromView(View view) {
        int width = (int) (view.getWidth() * BITMAP_SCALE);
        int height = (int) (view.getHeight() * BITMAP_SCALE);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable drawable = view.getBackground();
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public static TextInputLayout createTextBox(Context context, int hintResourceId) {
        TextInputLayout inputLayout = new TextInputLayout(context);
        LinearLayout.LayoutParams textInputLayoutParams = new LinearLayout.LayoutParams(UIUtils.dpToPx(context, 280), UIUtils.dpToPx(context, 48));

        int topMargin = UIUtils.dpToPx(context, 10);
        int horizontalMargin = UIUtils.dpToPx(context, 20);
        textInputLayoutParams.setMargins(horizontalMargin, topMargin, horizontalMargin, 0);
        inputLayout.setLayoutParams(textInputLayoutParams);
        int boxCornerRadius = UIUtils.dpToPx(context, 20);
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
        int horizontalPadding = UIUtils.dpToPx(inputLayout.getContext(), 15);
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

    public static CardView createDeviceCardView(Context context, Device device) {
        Resources resources = context.getResources();

        CardView card = new CardView(context);

        TextView deviceName = new TextView(context);
        deviceName.setText(device.getName());
        deviceName.setGravity(Gravity.START);

        Button expandToggleBtn = new Button(context);
        expandToggleBtn.setCompoundDrawables(null, null, ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_drop_down_24, context.getTheme()), null);
        expandToggleBtn.setGravity(Gravity.END);

        card.addView(deviceName);
        card.addView(expandToggleBtn);

        expandToggleBtn.setOnClickListener(new View.OnClickListener() {
            private boolean isOpened;

            @Override
            public void onClick(View view) {
                //TODO: UPDATE CARD
                if (isOpened) {
                    expandToggleBtn.setCompoundDrawables(null, null, ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_drop_down_24, context.getTheme()), null);
                } else {
                    expandToggleBtn.setCompoundDrawables(null, null, ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_drop_up_24, context.getTheme()), null);
                }
                isOpened = !isOpened;
            }
        });

        return card;
    }

    @SuppressWarnings({"MissingPermission"})
    public static View createDeviceListEntry(Context context, BluetoothDevice bluetoothDevice) {
        String deviceName = bluetoothDevice.getName();
        int generalDeviceType = bluetoothDevice.getBluetoothClass().getDeviceClass();

        ImageView deviceTypeIcon = new ImageView(context);
        deviceTypeIcon.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (generalDeviceType == BluetoothClass.Device.WEARABLE_WRIST_WATCH) {
            deviceTypeIcon.setImageResource(R.drawable.smartwatch_icon);
        }

        TextView deviceNameTextView = new TextView(context);
        deviceNameTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        deviceNameTextView.setText(deviceName);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(deviceTypeIcon);
        layout.addView(deviceNameTextView);

        return layout;
    }

    public static int pxToDp(Context context, float px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / displayMetrics.density);
    }

    public static int dpToPx(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * displayMetrics.density);
    }

    public static native Bitmap blur(Bitmap backgroundBitmap, int blurRadius);

    public static native Bitmap merge(Bitmap background, Bitmap componentUI, int xOffset, int yOffset);
}

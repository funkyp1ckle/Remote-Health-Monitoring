package com.pramit.rmh.device;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import androidx.fragment.app.DialogFragment;
import com.pramit.rmh.R;
import com.pramit.rmh.util.AWSUtils;
import com.pramit.rmh.util.BluetoothUtils;
import com.pramit.rmh.util.UIUtils;
import org.jetbrains.annotations.NotNull;

public class DeviceSelector extends DialogFragment {
    public static final String TAG = "Bluetooth Device Selector";
    private BluetoothUtils bluetoothUtils;
    private AWSUtils aws;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = requireContext();
        this.bluetoothUtils = BluetoothUtils.getInstance(context.getApplicationContext());
        bluetoothUtils.requestBluetooth(this);
        this.aws = AWSUtils.getInstance(context);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.DeviceSelectionFragment);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_add_device, null);
        LinearLayout layout = dialogView.findViewById(R.id.confirm_add_device_layout);
        dialogBuilder.setView(dialogView);
        Dialog dialog = dialogBuilder.create();
        dialog.setOnShowListener(dialogInterface -> {
            dialog.getWindow().setLayout(UIUtils.dpToPx(context, 350), UIUtils.dpToPx(context, 720));
            bluetoothUtils.startDiscovery();
        });
        return dialog;
    }

    public void updateUI(BluetoothDevice device) {

    }

    public void putInDB(Device device) {

    }
}

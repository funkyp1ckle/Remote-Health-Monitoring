package com.pramit.rhm.device;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.*;
import android.widget.LinearLayout;
import androidx.fragment.app.DialogFragment;
import com.pramit.rhm.R;
import com.pramit.rhm.util.AWSUtils;
import com.pramit.rhm.util.BluetoothUtils;
import com.pramit.rhm.util.UIUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DeviceSelector extends DialogFragment {
    public static final String TAG = "Bluetooth Device Selector";
    private BluetoothUtils bluetoothUtils;
    private AWSUtils aws;

    private static Device constructDevice(BluetoothDevice bluetoothDevice) {
        return null;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = requireContext();
        this.bluetoothUtils = BluetoothUtils.getInstance(context.getApplicationContext());
        bluetoothUtils.registerBluetoothListeners(this);
        this.aws = AWSUtils.getInstance(context);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.DeviceSelectionFragment);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_add_device, null);
        dialogBuilder.setView(dialogView);
        return dialogBuilder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = requireDialog();
        Window window = dialog.getWindow();
        Context context = requireContext();
        bluetoothUtils.requestBluetooth();
    }

    @Override
    public void onDismiss(@NotNull DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        bluetoothUtils.unregisterBluetoothListeners(this);
    }

    @SuppressLint("MissingPermission")
    public void updateUI(BluetoothDevice bluetoothDevice) {
        Dialog dialog = requireDialog();
        Context context = requireContext();
        View newDeviceView = UIUtils.createDeviceListEntry(context, bluetoothDevice);
        newDeviceView.setOnClickListener(view -> {
            ParcelUuid[] uuids = bluetoothDevice.getUuids();
            try {
                BluetoothSocket socket = bluetoothDevice.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                socket.connect();
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                Device device = constructDevice(bluetoothDevice);
                putInDB(device);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        dialog.getWindow().addContentView(newDeviceView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void putInDB(Device device) {

    }
}

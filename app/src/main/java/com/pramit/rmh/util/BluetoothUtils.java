package com.pramit.rmh.util;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class BluetoothUtils {
    private static BluetoothUtils bluetoothUtils;
    private final List<String> permissionsList;
    private final BluetoothManager bluetoothManager;
    private final BluetoothAdapter bluetoothAdapter;

    public BluetoothUtils(Context context) {
        this.bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        this.bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null)
            System.exit(0);
        this.permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.BLUETOOTH_SCAN);
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.BLUETOOTH_CONNECT);
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.BLUETOOTH);
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.BLUETOOTH_ADMIN);
        }
    }

    public static synchronized BluetoothUtils getInstance(Context context) {
        if (bluetoothUtils == null)
            bluetoothUtils = new BluetoothUtils(context);
        return bluetoothUtils;
    }

    public void startDiscovery() {
        try {
            bluetoothAdapter.startDiscovery();
        } catch (SecurityException exception) {
            exception.printStackTrace();
            System.exit(0);
        }
    }

    public void turnOnBluetooth(Fragment activity) {
        ActivityResultLauncher<Intent> turnOnBluetoothHandler = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() != Activity.RESULT_OK) {
                        System.exit(0);
                    }
                });
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            turnOnBluetoothHandler.launch(enableBtIntent);
        }
    }

    public void requestBluetooth(Fragment activity) {
        ActivityResultLauncher<String[]> bluetoothPermissionHandler = activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                isGranted -> {
                    if (isGranted.containsValue(false)) {
                        //TODO:PERSIST IN SHARED PREFS
                        System.exit(0);
                    } else {
                        turnOnBluetooth(activity);
                    }
                });
        bluetoothPermissionHandler.launch(permissionsList.toArray(new String[permissionsList.size()]));
    }
}

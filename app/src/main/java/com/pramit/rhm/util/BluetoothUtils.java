package com.pramit.rhm.util;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import com.pramit.rhm.device.DeviceSelector;

import java.util.ArrayList;
import java.util.List;

public class BluetoothUtils {
    private static BluetoothUtils bluetoothUtils;
    private final List<String> bluetoothPermissionsList;
    private final BluetoothManager bluetoothManager;
    private final BluetoothAdapter bluetoothAdapter;
    private static final List<Integer> supportedDevices = new ArrayList<>();

    static {
        supportedDevices.add(BluetoothClass.Device.HEALTH_GLUCOSE);
        supportedDevices.add(BluetoothClass.Device.HEALTH_BLOOD_PRESSURE);
        supportedDevices.add(BluetoothClass.Device.HEALTH_PULSE_RATE);
        supportedDevices.add(BluetoothClass.Device.HEALTH_DATA_DISPLAY);
        supportedDevices.add(BluetoothClass.Device.HEALTH_THERMOMETER);
        supportedDevices.add(BluetoothClass.Device.HEALTH_PULSE_OXIMETER);
        supportedDevices.add(BluetoothClass.Device.HEALTH_UNCATEGORIZED);
        supportedDevices.add(BluetoothClass.Device.HEALTH_WEIGHING);
        supportedDevices.add(BluetoothClass.Device.WEARABLE_WRIST_WATCH);
    }
    private ActivityResultLauncher<String[]> bluetoothPermissionHandler;
    private ActivityResultLauncher<String[]> locationPermissionHandler;
    private ActivityResultLauncher<Intent> turnOnBluetoothHandler;
    private BluetoothDiscoveryResponseHandler bluetoothDiscoveryResponseHandler;

    public BluetoothUtils(Context context) {
        this.bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        this.bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null)
            System.exit(0);
        this.bluetoothPermissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED)
                bluetoothPermissionsList.add(Manifest.permission.BLUETOOTH_SCAN);
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
                bluetoothPermissionsList.add(Manifest.permission.BLUETOOTH_CONNECT);
        }
    }

    public static synchronized BluetoothUtils getInstance(Context context) {
        if (bluetoothUtils == null)
            bluetoothUtils = new BluetoothUtils(context);
        return bluetoothUtils;
    }

    public static boolean isDeviceSupported(int deviceType) {
        return supportedDevices.contains(deviceType);
    }

    public void startDiscovery() {
        try {
            if (bluetoothAdapter.isDiscovering())
                bluetoothAdapter.cancelDiscovery();
            bluetoothAdapter.startDiscovery();
        } catch (SecurityException exception) {
            exception.printStackTrace();
            System.exit(0);
        }
    }

    public void registerBluetoothListeners(DeviceSelector fragment) {
        bluetoothPermissionHandler = fragment.registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                        if (!isGranted.containsValue(false)) {
                            if(!bluetoothAdapter.isEnabled()) {
                                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                turnOnBluetoothHandler.launch(enableBtIntent);
                            } else {
                                startDiscovery();
                            }
                        } else {
                            //TODO:PERSIST IN SHARED PREFS
                            System.exit(0);
                        }
                    });
        locationPermissionHandler = fragment.registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                        if (!isGranted.containsValue(false)) {
                            if(!bluetoothAdapter.isEnabled()) {
                                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                turnOnBluetoothHandler.launch(enableBtIntent);
                            } else {
                                startDiscovery();
                            }
                        } else {
                            //TODO:PERSIST IN SHARED PREFS
                            System.exit(0);
                        }
                    });
        turnOnBluetoothHandler = fragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        startDiscovery();
                    } else {
                        //TODO:PERSIST IN SHARED PREFS
                        System.exit(0);
                    }
                });

        this.bluetoothDiscoveryResponseHandler = new BluetoothDiscoveryResponseHandler(fragment);
        Activity fragmentActivity = fragment.requireActivity();
        IntentFilter deviceFoundFilter = new IntentFilter();
        deviceFoundFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        deviceFoundFilter.addAction(BluetoothDevice.ACTION_FOUND);
        deviceFoundFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        fragmentActivity.registerReceiver(bluetoothDiscoveryResponseHandler, deviceFoundFilter);
    }

    public void unregisterBluetoothListeners(DeviceSelector fragment) {
        Activity fragmentActivity = fragment.requireActivity();
        bluetoothPermissionHandler.unregister();
        turnOnBluetoothHandler.unregister();
        fragmentActivity.unregisterReceiver(bluetoothDiscoveryResponseHandler);
    }

    public void requestBluetooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            bluetoothPermissionHandler.launch(bluetoothPermissionsList.toArray(new String[0]));
        else
            locationPermissionHandler.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
    }
}

class BluetoothDiscoveryResponseHandler extends BroadcastReceiver {
    private final DeviceSelector curDeviceSelector;
    public BluetoothDiscoveryResponseHandler(DeviceSelector deviceSelector) {
        this.curDeviceSelector = deviceSelector;
    }
    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
            System.out.println("started");
        else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            System.out.println("ended");
        else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            System.out.println("found");
            BluetoothDevice device = null;
            if (android.os.Build.VERSION.SDK_INT >= 33)
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice.class);
            else
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (device != null && BluetoothUtils.isDeviceSupported(device.getBluetoothClass().getDeviceClass()) && device.getBondState() != BluetoothDevice.BOND_BONDED)
                curDeviceSelector.updateUI(device);
        }
    }
}

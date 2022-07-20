package com.pramit.rmh;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.pramit.rmh.deviceModel.Device;
import com.pramit.rmh.deviceModel.DeviceSelector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class Home extends AppCompatActivity {
    private String userId;
    private AWSConnection aws;
    private ArrayList<Device> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.userId = getIntent().getExtras().getString("user_id");
        this.aws = AWSConnection.getInstance(this);
        this.devices = new ArrayList<>();
        getDevices();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(@NotNull Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int btnResId = item.getItemId();
        if (btnResId == R.id.viewAccountBtn) {
            DialogFragment deviceSelector = new DeviceSelector();
            deviceSelector.show(getSupportFragmentManager(), DeviceSelector.TAG);
            return true;
        } else if (btnResId == R.id.addDeviceBtn) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void addDeviceToUI(Device device) {
        devices.add(device);
        CardView deviceCard = UIUtils.createDeviceCardView(this, device);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout devicesContainer = findViewById(R.id.devicesContainer);
        devicesContainer.addView(deviceCard, cardParams);
    }

    public void getDevices() {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("user_id", new AttributeValue(userId));
        GetItemRequest request = new GetItemRequest(AWSConnection.DB_TABLE_NAME, key);

        List<String> attributesToGet = new ArrayList<>();
        attributesToGet.add("devices");
        request.setAttributesToGet(attributesToGet);

        Future<GetItemResult> userInfo = aws.getItem(request, new AsyncHandler<GetItemRequest, GetItemResult>() {
            @Override
            public void onError(Exception exception) {

            }

            @Override
            public void onSuccess(GetItemRequest request, GetItemResult getItemResult) {
                Map<String, AttributeValue> resultValue = getItemResult.getItem();
                AttributeValue deviceList = resultValue.get("devices");
                assert deviceList != null;
                Map<String, AttributeValue> map = deviceList.getM();
                List<Device> devices = new ArrayList<>();
                for (Map.Entry<String, AttributeValue> mapEntry : map.entrySet())
                    devices.add(aws.dynamoMapToPOJO(Device.class, mapEntry.getValue().getM()));

                for (Device curDevice : devices) {
                    addDeviceToUI(curDevice);
                }
            }
        });
    }
}

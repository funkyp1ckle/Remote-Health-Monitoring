package com.pramit.rmh;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    private AWSConnection aws;
    private Device[] devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.aws = AWSConnection.getInstance(this);
        this.devices = getDevices(); //TODO: MAYBE MAKE COMPLETELY ASYNC
        initListeners();
    }

    public void initUI() {
        for (Device device : devices) {

        }
    }

    public void initListeners() {
        findViewById(R.id.root).getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            initUI();
            //ADD LISTENERS HERE
        });
    }

    public Device[] getDevices() {
        //Map<String, AttributeValue> query = new HashMap<>();
        //Future<GetItemResult> dbGetResult = aws.getItems(query);
        //GetItemResult result = dbGetResult.whenCompleteAsync();
        return new Device[]{};
    }
}

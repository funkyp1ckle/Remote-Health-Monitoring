package com.pramit.rmh;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    private AWSConnection aws;
    private Device[] devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (Build.VERSION.SDK_INT >= 33)
            this.aws = getIntent().getSerializableExtra("AWS", AWSConnection.class);
        else
            this.aws = (AWSConnection) getIntent().getSerializableExtra("AWS");
        this.devices = getDevices();
        init();
    }

    public void init() {
        findViewById(R.id.root).getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            initUI();
            initListeners();
        });
    }

    public void initUI() {
        for (Device device : devices) {

        }
    }

    public void initListeners() {

    }

    public Device[] getDevices() {
        return null;
    }
}

package com.pramit.rmh.deviceModel;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;

public abstract class Device {
    private String name;
    private byte[] data;

    public Device() {
        setName();
        setData();
    }

    @DynamoDBAttribute(attributeName = "deviceName")
    public String getName() {
        return name;
    }

    protected abstract void setName();

    @DynamoDBAttribute(attributeName = "deviceData")
    public byte[] getData() {
        return data;
    }

    protected abstract void setData();
}

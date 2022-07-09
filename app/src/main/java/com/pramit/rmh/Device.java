package com.pramit.rmh;

public abstract class Device {
    private String name;
    private byte[] data;

    public Device() {
        setName();
        setData();
    }

    public String getName() {
        return name;
    }

    protected abstract void setName();

    public byte[] getData() {
        return data;
    }

    protected abstract void setData();
}

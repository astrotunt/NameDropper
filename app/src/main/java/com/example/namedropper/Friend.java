package com.example.namedropper;

public class Friend {
    private int id;
    private String name;
    private String phoneNumber;
    private int frequency;

    public Friend(int id, String name, String phoneNumber, int frequency) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.frequency = frequency;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public int getFrequency() {
        return frequency;
    }
}

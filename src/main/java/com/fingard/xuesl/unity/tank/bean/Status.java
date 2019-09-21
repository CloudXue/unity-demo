package com.fingard.xuesl.unity.tank.bean;

public enum Status {
    PREPARE(0),
    FIGHT(1);

    private int value;

    Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

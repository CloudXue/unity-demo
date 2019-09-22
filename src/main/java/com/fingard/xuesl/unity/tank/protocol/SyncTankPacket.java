package com.fingard.xuesl.unity.tank.protocol;

import lombok.Data;
import lombok.ToString;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/22/022<br>
 * <br>
 */
@Data
@ToString
public class SyncTankPacket implements Packet {
    @Override
    public String getProtocolName() {
        return "MsgSyncTank";
    }

    //位置、旋转、炮塔旋转
    public float x = 0f;
    public float y = 0f;
    public float z = 0f;
    public float ex = 0f;
    public float ey = 0f;
    public float ez = 0f;
    public float turretY = 0f;

    public String id = "";		//哪个坦克
}

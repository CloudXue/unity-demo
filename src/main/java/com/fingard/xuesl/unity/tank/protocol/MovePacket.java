package com.fingard.xuesl.unity.tank.protocol;

import lombok.Data;
import lombok.ToString;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/10/010<br>
 * <br>
 */
@Data
@ToString
public class MovePacket implements Packet {
    private String protocolName = "MsgMove";
    private float x;
    private float y;
    private float z;
}

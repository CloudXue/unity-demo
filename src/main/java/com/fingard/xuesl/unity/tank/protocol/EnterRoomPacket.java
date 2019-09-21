package com.fingard.xuesl.unity.tank.protocol;

import lombok.Data;
import lombok.ToString;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/21/021<br>
 * <br>
 */
@ToString
@Data
public class EnterRoomPacket implements Packet {
    @Override
    public String getProtocolName() {
        return "MsgEnterRoom";
    }

    private int id = 0;
    private int result = 0;
}

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
public class GetAchievePacket implements Packet {

    @Override
    public String getProtocolName() {
        return "MsgGetAchieve";
    }

    private int win = 0;
    private int lost = 0;
}

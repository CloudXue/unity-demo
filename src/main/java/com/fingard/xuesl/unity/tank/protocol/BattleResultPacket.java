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
public class BattleResultPacket implements Packet {
    @Override
    public String getProtocolName() {
        return "MsgBattleResult";
    }

    private int winCamp = 0;
}

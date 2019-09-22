package com.fingard.xuesl.unity.tank.protocol;

import com.fingard.xuesl.unity.tank.bean.TankInfo;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/22/022<br>
 * <br>
 */
@ToString
@Data
public class EnterBattlePacket implements Packet {
    @Override
    public String getProtocolName() {
        return "MsgEnterBattle";
    }

    private List<TankInfo> tanks;
    private int mapId = 1;
}

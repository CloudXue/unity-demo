package com.fingard.xuesl.unity.tank.protocol;

import com.fingard.xuesl.unity.tank.bean.PlayerInfo;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/21/021<br>
 * <br>
 */
@ToString
@Data
public class GetRoomInfoPacket implements Packet {
    @Override
    public String getProtocolName() {
        return "MsgGetRoomInfo";
    }

    public List<PlayerInfo> players;
}

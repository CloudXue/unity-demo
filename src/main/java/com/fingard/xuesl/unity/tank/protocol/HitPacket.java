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
public class HitPacket implements Packet {
    @Override
    public String getProtocolName() {
        return "MsgHit";
    }
    //坦克ID
    public String id = "";
    //被击中坦克血量
    public int hp = 0;
    //受到的伤害
    public int damage = 0;
    //击中谁
    public String targetId = "";
    //击中点
    public float x = 0f;
    public float y = 0f;
    public float z = 0f;

}

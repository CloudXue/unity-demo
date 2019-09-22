package com.fingard.xuesl.unity.tank.bean;

import com.fingard.xuesl.unity.tank.protocol.Packet;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/21/021<br>
 * <br>
 */
@Slf4j
@Data
public class Player {

    public String id = "";
    public ClientState state;
    public Player(ClientState state) {
        this.state = state;
    }

    //坐标
    public float x;
    public float y;
    public float z;

    public float ex;
    public float ey;
    public float ez;
    //在哪个房间
    public int roomId = -1;
    //阵营
    public int camp = 1;
    //坦克生命值
    public int hp = 100;

    public PlayerData data;

    //发送信息
    public void send(Packet packet){
        log.info("send " + packet.toString());
        state.getChannel().writeAndFlush(packet);
    }
}

package com.fingard.xuesl.unity.tank.protocol;

import io.netty.channel.Channel;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/15/015<br>
 * <br>
 */
public class ClientState {
    private Channel channel;
    private String desc;
    private int hp = -100;
    private float x;
    private float y;
    private float z;
    private float eulY;
}

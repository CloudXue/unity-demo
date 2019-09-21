package com.fingard.xuesl.unity.tank.bean;

import io.netty.channel.Channel;
import lombok.Data;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/15/015<br>
 * <br>
 */
@Data
public class ClientState {
    private Channel channel;
    private String desc;
    private Player player;
}

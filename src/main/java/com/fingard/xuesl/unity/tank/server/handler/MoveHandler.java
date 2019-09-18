package com.fingard.xuesl.unity.tank.server.handler;

import com.fingard.xuesl.unity.tank.protocol.MovePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/11/011<br>
 * <br>
 */
@Slf4j
public class MoveHandler extends SimpleChannelInboundHandler<MovePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MovePacket movePacket) throws Exception {
        log.info("move:" + movePacket);
    }
}

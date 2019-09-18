package com.fingard.xuesl.unity.tank.server.handler;

import cn.hutool.core.util.StrUtil;
import com.fingard.xuesl.unity.online.protocol.ClientState;
import com.fingard.xuesl.unity.tank.protocol.LoginPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/15/015<br>
 * <br>
 */
@Slf4j
public class LoginHandler extends SimpleChannelInboundHandler<LoginPacket> {
    public static Map<String, ClientState> clientMap = new ConcurrentHashMap<String, ClientState>(8);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("active:" + ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginPacket msg) throws Exception {
        ClientState clientState = new ClientState();
        clientState.setChannel(ctx.channel());
        clientState.setDesc(msg.getDesc());
        clientState.setX(msg.getX());
        clientState.setY(msg.getY());
        clientState.setZ(msg.getZ());
        clientMap.put(clientState.getDesc(), clientState);
        for (Map.Entry<String, ClientState> entry : clientMap.entrySet()) {
            ReferenceCountUtil.retain(msg);
            entry.getValue().getChannel().writeAndFlush(msg);
            log.info("send:" + clientState.getDesc());
        }
        ReferenceCountUtil.release(msg);
    }
}

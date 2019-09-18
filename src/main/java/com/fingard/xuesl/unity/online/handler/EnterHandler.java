package com.fingard.xuesl.unity.online.handler;

import cn.hutool.core.util.StrUtil;
import com.fingard.xuesl.unity.online.protocol.ClientState;
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
 * 开发时间: 2019/9/3/003<br>
 * <br>
 */
@Slf4j
public class EnterHandler extends SimpleChannelInboundHandler<String> {
    public static Map<String, ClientState> clientMap = new ConcurrentHashMap<String, ClientState>(8);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("active:" + ctx.channel().remoteAddress());
    }

    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        String[] strings = StrUtil.split(msg, "|");
        if (strings.length != 2 || !StrUtil.equals("Enter", strings[0])) {
            ctx.fireChannelRead(msg);
            return;
        }
        ClientState clientState = new ClientState();
        String[] args = StrUtil.split(strings[1], ",");
        clientState.setChannel(ctx.channel());
        clientState.setDesc(args[0]);
        clientState.setX(Float.parseFloat(args[1]));
        clientState.setY(Float.parseFloat(args[2]));
        clientState.setZ(Float.parseFloat(args[3]));
        clientMap.put(clientState.getDesc(), clientState);
        for (Map.Entry<String, ClientState> entry : clientMap.entrySet()) {
            ReferenceCountUtil.retain(msg);
            entry.getValue().getChannel().writeAndFlush(msg);
            log.info("send:" + clientState.getDesc());
        }
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("inactive:" + ctx.channel().remoteAddress());
    }
}

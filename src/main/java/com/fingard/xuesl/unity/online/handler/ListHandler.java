package com.fingard.xuesl.unity.online.handler;

import cn.hutool.core.util.StrUtil;
import com.fingard.xuesl.unity.online.protocol.ClientState;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/3/003<br>
 * <br>
 */
@Slf4j
public class ListHandler extends SimpleChannelInboundHandler<String> {
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("list msg:" + msg);
        if (!StrUtil.equals("List|", msg)) {
            ctx.fireChannelRead(msg);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder("List|");
        for (Map.Entry<String, ClientState> entry : EnterHandler.clientMap.entrySet()) {
            ClientState clientState = entry.getValue();
            stringBuilder
                    .append(clientState.getDesc())
                    .append(",")
                    .append(clientState.getX())
                    .append(",")
                    .append(clientState.getY())
                    .append(",")
                    .append(clientState.getZ())
                    .append(",")
                    .append(clientState.getEulY())
                    .append(",")
                    .append(clientState.getHp())
                    .append(",");
        }
        log.info("List msg:" + stringBuilder.toString());
        ctx.channel().writeAndFlush(stringBuilder.toString());
    }
}

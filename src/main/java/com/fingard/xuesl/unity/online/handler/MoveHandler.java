package com.fingard.xuesl.unity.online.handler;

import cn.hutool.core.util.StrUtil;
import com.fingard.xuesl.unity.online.protocol.ClientState;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import java.util.Map;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/4/004<br>
 * <br>
 */
public class MoveHandler extends SimpleChannelInboundHandler<String> {
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        String[] strings = StrUtil.split(msg, "|");
        if (strings.length != 2 || !StrUtil.equals("Move", strings[0])) {
            ctx.fireChannelRead(msg);
            return;
        }
        String[] args = StrUtil.split(strings[1], ",");
        String desc = args[0];
        ClientState clientState = EnterHandler.clientMap.get(desc);
        if (clientState != null) {
            float x = Float.parseFloat(args[1]);
            float y = Float.parseFloat(args[2]);
            float z = Float.parseFloat(args[3]);
            clientState.setX(x);
            clientState.setY(y);
            clientState.setZ(z);

            for (Map.Entry<String, ClientState> entry : EnterHandler.clientMap.entrySet()) {
                ReferenceCountUtil.retain(msg);
                entry.getValue().getChannel().writeAndFlush(msg);
            }
            ReferenceCountUtil.release(msg);
        }

    }
}

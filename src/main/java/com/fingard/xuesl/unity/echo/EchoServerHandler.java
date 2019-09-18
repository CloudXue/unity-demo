package com.fingard.xuesl.unity.echo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.ReferenceCountUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/8/30/030<br>
 * <br>
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    private static Map<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>(8);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("active:" + ctx.channel().remoteAddress());
        channelMap.put(ctx.channel().remoteAddress().toString(), ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        for (Map.Entry<String, Channel> entry : channelMap.entrySet()) {
            ReferenceCountUtil.retain(msg);
            entry.getValue().writeAndFlush(msg);
            System.out.println("send:" + entry.getValue().remoteAddress());
        }
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}

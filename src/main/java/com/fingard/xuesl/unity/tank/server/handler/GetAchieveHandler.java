package com.fingard.xuesl.unity.tank.server.handler;

import com.fingard.xuesl.unity.tank.bean.ClientState;
import com.fingard.xuesl.unity.tank.bean.Player;
import com.fingard.xuesl.unity.tank.protocol.GetAchievePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/21/021<br>
 * <br>
 */
public class GetAchieveHandler extends SimpleChannelInboundHandler<GetAchievePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GetAchievePacket msg) throws Exception {
        ClientState clientState = LoginHandler.clientMap.get(ctx.channel());
        Player player = clientState.getPlayer();
        if (player == null) {
            return;
        }
        msg.setWin(player.getData().getWin());
        msg.setLost(player.getData().getLost());

        player.send(msg);
    }
}

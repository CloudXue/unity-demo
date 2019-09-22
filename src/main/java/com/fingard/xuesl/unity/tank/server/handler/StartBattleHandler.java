package com.fingard.xuesl.unity.tank.server.handler;

import com.fingard.xuesl.unity.tank.bean.ClientState;
import com.fingard.xuesl.unity.tank.bean.Player;
import com.fingard.xuesl.unity.tank.bean.Room;
import com.fingard.xuesl.unity.tank.protocol.StartBattlePacket;
import com.fingard.xuesl.unity.tank.util.RoomManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/22/022<br>
 * <br>
 */
public class StartBattleHandler extends SimpleChannelInboundHandler<StartBattlePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StartBattlePacket msg) throws Exception {
        ClientState clientState = LoginHandler.clientMap.get(ctx.channel());
        Player player = clientState.getPlayer();
        if (player == null) {
            return;
        }

        //Room
        Room room = RoomManager.getRoom(player.getRoomId());
        if (room == null) {
            msg.setResult(1);
            player.send(msg);
            return;
        }

        //是否为房主
        if (!room.isOwner(player)) {
            msg.setResult(1);
            player.send(msg);
            return;
        }

        //开始
        if (!room.startBattle()) {
            msg.setResult(1);
            player.send(msg);
            return;
        }

        msg.setResult(0);
        player.send(msg);
    }
}

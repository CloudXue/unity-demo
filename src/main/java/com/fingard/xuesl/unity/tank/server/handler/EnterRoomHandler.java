package com.fingard.xuesl.unity.tank.server.handler;

import com.fingard.xuesl.unity.tank.bean.ClientState;
import com.fingard.xuesl.unity.tank.bean.Player;
import com.fingard.xuesl.unity.tank.bean.Room;
import com.fingard.xuesl.unity.tank.protocol.EnterRoomPacket;
import com.fingard.xuesl.unity.tank.util.RoomManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/21/021<br>
 * <br>
 */
public class EnterRoomHandler extends SimpleChannelInboundHandler<EnterRoomPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, EnterRoomPacket msg) throws Exception {
        ClientState clientState = LoginHandler.clientMap.get(ctx.channel());
        Player player = clientState.getPlayer();
        if (player == null) {
            return;
        }

        //已经在房里
        if (player.getRoomId() >= 0) {
            msg.setResult(1);
            player.send(msg);
            return;
        }

        //获取房间
        Room room = RoomManager.getRoom(msg.getId());
        if (room == null) {
            msg.setResult(1);
            player.send(msg);
            return;
        }

        //进入
        if (!room.addPlayer(player.getId())) {
            msg.setResult(1);
            player.send(msg);
            return;
        }

        //返回
        msg.setResult(0);
        player.send(msg);
    }
}

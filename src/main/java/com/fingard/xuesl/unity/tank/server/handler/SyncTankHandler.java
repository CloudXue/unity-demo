package com.fingard.xuesl.unity.tank.server.handler;

import com.fingard.xuesl.unity.tank.bean.ClientState;
import com.fingard.xuesl.unity.tank.bean.Player;
import com.fingard.xuesl.unity.tank.bean.Room;
import com.fingard.xuesl.unity.tank.bean.Status;
import com.fingard.xuesl.unity.tank.protocol.SyncTankPacket;
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
public class SyncTankHandler extends SimpleChannelInboundHandler<SyncTankPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SyncTankPacket msg) throws Exception {
        ClientState clientState = LoginHandler.clientMap.get(ctx.channel());
        Player player = clientState.getPlayer();
        if (player == null) {
            return;
        }
        //room
        Room room = RoomManager.getRoom(player.roomId);
        if(room == null){
            return;
        }
        //status
        if(room.status != Status.FIGHT.getValue()){
            return;
        }
        //是否作弊
//        if(Math.Abs(player.x - msg.x) > 5 ||
//                Math.Abs(player.y - msg.y) > 5 ||
//                Math.Abs(player.z - msg.z) > 5){
//            Console.WriteLine("疑似作弊 " + player.id);
//        }
        //更新信息
        player.x = msg.x;
        player.y = msg.y;
        player.z = msg.z;
        player.ex = msg.ex;
        player.ey = msg.ey;
        player.ez = msg.ez;
        //广播
        msg.id = player.id;
        room.broadcast(msg);
    }
}

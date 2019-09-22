package com.fingard.xuesl.unity.tank.server.handler;

import com.fingard.xuesl.unity.tank.bean.ClientState;
import com.fingard.xuesl.unity.tank.bean.Player;
import com.fingard.xuesl.unity.tank.bean.Room;
import com.fingard.xuesl.unity.tank.bean.Status;
import com.fingard.xuesl.unity.tank.protocol.HitPacket;
import com.fingard.xuesl.unity.tank.util.PlayerManager;
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
public class HitHandler extends SimpleChannelInboundHandler<HitPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HitPacket msg) throws Exception {
        ClientState clientState = LoginHandler.clientMap.get(ctx.channel());
        Player player = clientState.getPlayer();
        if (player == null) {
            return;
        }
        //targetPlayer
        Player targetPlayer = PlayerManager.getPlayer(msg.targetId);
        if(targetPlayer == null){
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
        //发送者校验
        if(!player.id.equals(msg.id)){
            return;
        }
        //状态
        int damage = 35;
        targetPlayer.hp -= damage;
        //广播
        msg.id = player.id;
        msg.hp = player.hp;
        msg.damage = damage;
        room.broadcast(msg);
    }
}

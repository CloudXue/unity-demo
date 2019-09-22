package com.fingard.xuesl.unity.tank.server.handler;

import com.fingard.xuesl.unity.tank.bean.ClientState;
import com.fingard.xuesl.unity.tank.bean.Player;
import com.fingard.xuesl.unity.tank.bean.PlayerData;
import com.fingard.xuesl.unity.tank.bean.Room;
import com.fingard.xuesl.unity.tank.protocol.LoginPacket;
import com.fingard.xuesl.unity.tank.util.PlayerManager;
import com.fingard.xuesl.unity.tank.util.RoomManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
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
    public static Map<Channel, ClientState> clientMap = new ConcurrentHashMap<>(8);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("active:" + ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginPacket msg) throws Exception {
        ClientState clientState = new ClientState();
        clientState.setChannel(ctx.channel());
        clientState.setDesc(msg.getDesc());
        //构建Player
        Player player = new Player(clientState);
        player.id = msg.getId();
        PlayerData playerData = new PlayerData();
        playerData.setWin(0);
        playerData.setLost(0);
        playerData.setCoin(0);
        player.setData(playerData);
        PlayerManager.addPlayer(msg.getId(), player);
        clientState.setPlayer(player);

        clientMap.put(ctx.channel(), clientState);
        //返回协议
        msg.setResult(0);
        player.send(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ClientState clientState = LoginHandler.clientMap.get(ctx.channel());
        Player player = clientState.getPlayer();
        log.info("客户端断开连接：" + player.getId());
        if (player == null) {
            return;
        }

        int roomId = player.getRoomId();
        if (roomId >= 0) {
            Room room = RoomManager.getRoom(roomId);
            room.removePlayer(player.getId());
        }
        PlayerManager.removePlayer(player.getId());
    }
}

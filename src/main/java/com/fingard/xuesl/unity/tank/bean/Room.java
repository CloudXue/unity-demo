package com.fingard.xuesl.unity.tank.bean;

import com.fingard.xuesl.unity.tank.protocol.GetRoomInfoPacket;
import com.fingard.xuesl.unity.tank.protocol.Packet;
import com.fingard.xuesl.unity.tank.util.PlayerManager;
import com.fingard.xuesl.unity.tank.util.RoomManager;
import io.netty.util.ReferenceCountUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/21/021<br>
 * <br>
 */
@Data
@Slf4j
public class Room {
    //id
    public int id = 0;
    //最大玩家数
    public int maxPlayer = 6;
    //玩家列表
    public Map<String, Boolean> playerIds = new ConcurrentHashMap<>();
    //房主id
    public String ownerId = "";

    public int status = Status.PREPARE.getValue();

    //添加玩家
    public Boolean addPlayer(String id){
        //获取玩家
        Player player = PlayerManager.getPlayer(id);
        if(player == null){
            log.info("room.addPlayer fail, player is null");
            return false;
        }
        //房间人数
        if(playerIds.size() >= maxPlayer){
            log.info("room.addPlayer fail, reach maxPlayer");
            return false;
        }
        //准备状态才能加人
        if(status != Status.PREPARE.getValue()){
            log.info("room.addPlayer fail, not PREPARE");
            return false;
        }
        //已经在房间里
        if(playerIds.containsKey(id)){
            log.info("room.addPlayer fail, already in this room");
            return false;
        }
        //加入列表
        playerIds.put(id, true);
        //设置玩家数据
        player.camp = switchCamp();
        player.roomId = this.id;
        //设置房主
        if(ownerId.equals("")){
            ownerId = player.id;
        }
        //广播
        broadcast(toMsg());
        return true;
    }

    //分配阵营
    public int switchCamp() {
        //计数
        int count1 = 0;
        int count2 = 0;
        for(String id : playerIds.keySet()) {
            Player player = PlayerManager.getPlayer(id);
            if(player.camp == 1) {count1++;}
            if(player.camp == 2) {count2++;}
        }
        //选择
        if (count1 <= count2){
            return 1;
        }
        else{
            return 2;
        }
    }

    //是不是房主
    public Boolean isOwner(Player player){
        return player.id.equals(ownerId);
    }

    //删除玩家
    public Boolean removePlayer(String id) {
        //获取玩家
        Player player = PlayerManager.getPlayer(id);
        if(player == null){
            log.info("room.removePlayer fail, player is null");
            return false;
        }
        //没有在房间里
        if(!playerIds.containsKey(id)){
            log.info("room.removePlayer fail, not in this room");
            return false;
        }
        //删除列表
        playerIds.remove(id);
        //设置玩家数据
        player.camp = 0;
        player.roomId = -1;
        //设置房主
        if(ownerId == player.id){
            ownerId = switchOwner();
        }
        //房间为空
        if(playerIds.size() == 0){
            RoomManager.removeRoom(this.id);
        }
        //广播
        broadcast(toMsg());
        return true;
    }

    //选择房主
    public String switchOwner() {
        //选择第一个玩家
        for(String id : playerIds.keySet()) {
            return id;
        }
        //房间没人
        return "";
    }


    //广播消息
    public void broadcast(Packet packet){
        for(String id : playerIds.keySet()) {
            Player player = PlayerManager.getPlayer(id);
            ReferenceCountUtil.retain(packet);
            player.send(packet);
        }
        ReferenceCountUtil.release(packet);
    }

    //生成MsgGetRoomInfo协议
    public Packet toMsg(){
        GetRoomInfoPacket packet = new GetRoomInfoPacket();
        packet.players = new ArrayList<>();
        //players
        for (String id : playerIds.keySet()) {
            Player player = PlayerManager.getPlayer(id);
            PlayerInfo playerInfo = new PlayerInfo();
            //赋值
            playerInfo.setId(player.id);
            playerInfo.setCamp(player.camp);
            playerInfo.setWin(player.data.win);
            playerInfo.setLost(player.data.lost);
            playerInfo.setIsOwner(0);
            if(isOwner(player)){
                playerInfo.setIsOwner(1);
            }
            packet.players.add(playerInfo);
        }
        return packet;
    }

}


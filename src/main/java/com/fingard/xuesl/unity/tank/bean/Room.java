package com.fingard.xuesl.unity.tank.bean;

import com.fingard.xuesl.unity.tank.protocol.*;
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

    private long lastJudgeTime = 0;

    public static float[][][] birthConfig = {
            {
                    {-70f, 0.5f, -70f, 0, 0, 0},
                    {-50f, 0.5f, -70f, 0, 0, 0},
                    {-30f, 0.5f, -40f, 0, 0, 0}
            },
            {
                    {70f, 0.5f, 70f, 0, 180f, 0},
                    {50f, 0.5f, 70f, 0, 180f, 0},
                    {30f, 0.5f, 70f, 0, 180f, 0}
            }
    };

    //添加玩家
    public Boolean addPlayer(String id) {
        //获取玩家
        Player player = PlayerManager.getPlayer(id);
        if (player == null) {
            log.info("room.addPlayer fail, player is null");
            return false;
        }
        //房间人数
        if (playerIds.size() >= maxPlayer) {
            log.info("room.addPlayer fail, reach maxPlayer");
            return false;
        }
        //准备状态才能加人
        if (status != Status.PREPARE.getValue()) {
            log.info("room.addPlayer fail, not PREPARE");
            return false;
        }
        //已经在房间里
        if (playerIds.containsKey(id)) {
            log.info("room.addPlayer fail, already in this room");
            return false;
        }
        //加入列表
        playerIds.put(id, true);
        //设置玩家数据
        player.camp = switchCamp();
        player.roomId = this.id;
        //设置房主
        if (ownerId.equals("")) {
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
        for (String id : playerIds.keySet()) {
            Player player = PlayerManager.getPlayer(id);
            if (player.camp == 1) {
                count1++;
            }
            if (player.camp == 2) {
                count2++;
            }
        }
        //选择
        if (count1 <= count2) {
            return 1;
        } else {
            return 2;
        }
    }

    //是不是房主
    public Boolean isOwner(Player player) {
        return player.id.equals(ownerId);
    }

    //删除玩家
    public Boolean removePlayer(String id) {
        //获取玩家
        Player player = PlayerManager.getPlayer(id);
        if (player == null) {
            log.info("room.removePlayer fail, player is null");
            return false;
        }
        //没有在房间里
        if (!playerIds.containsKey(id)) {
            log.info("room.removePlayer fail, not in this room");
            return false;
        }
        //删除列表
        playerIds.remove(id);
        //设置玩家数据
        player.camp = 0;
        player.roomId = -1;
        //设置房主
        if (ownerId == player.id) {
            ownerId = switchOwner();
        }
        //战斗状态退出
        if (status == Status.FIGHT.getValue()) {
            player.data.lost++;
            LeaveBattlePacket msg = new LeaveBattlePacket();
            msg.setId(player.getId());
            broadcast(msg);
        }
        //房间为空
        if (playerIds.size() == 0) {
            RoomManager.removeRoom(this.id);
        }
        //广播
        broadcast(toMsg());
        return true;
    }

    //选择房主
    public String switchOwner() {
        //选择第一个玩家
        for (String id : playerIds.keySet()) {
            return id;
        }
        //房间没人
        return "";
    }


    //广播消息
    public void broadcast(Packet packet) {
        for (String id : playerIds.keySet()) {
            Player player = PlayerManager.getPlayer(id);
            ReferenceCountUtil.retain(packet);
            player.send(packet);
        }
        ReferenceCountUtil.release(packet);
    }

    //生成MsgGetRoomInfo协议
    public Packet toMsg() {
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
            if (isOwner(player)) {
                playerInfo.setIsOwner(1);
            }
            packet.players.add(playerInfo);
        }
        return packet;
    }

    public boolean canStartBattle() {
        //已经是战斗状态
        if (status != Status.PREPARE.getValue()) {
            return false;
        }
        int count1 = 0;
        int count2 = 0;
        for (String id : playerIds.keySet()) {
            Player player = PlayerManager.getPlayer(id);
            if (player.camp == 1) {
                count1++;
            } else {
                count2++;
            }
        }
        //每个阵营至少有一个玩家
        if (count1 < 1 || count2 < 1) {
            return false;
        }

        return true;
    }

    //初始化位置
    private void setBirthPos(Player player, int index) {
        int camp = player.camp;

        player.x = birthConfig[camp - 1][index][0];
        player.y = birthConfig[camp - 1][index][1];
        player.z = birthConfig[camp - 1][index][2];
        player.ex = birthConfig[camp - 1][index][3];
        player.ey = birthConfig[camp - 1][index][4];
        player.ez = birthConfig[camp - 1][index][5];
    }

    //玩家数据转成TankInfo
    public TankInfo playerToTankInfo(Player player) {
        TankInfo tankInfo = new TankInfo();
        tankInfo.setCamp(player.getCamp());
        tankInfo.setId(player.getId());
        tankInfo.setHp(player.getHp());

        tankInfo.setX(player.getX());
        tankInfo.setY(player.getY());
        tankInfo.setZ(player.getZ());
        tankInfo.setEx(player.getEx());
        tankInfo.setEy(player.getEy());
        tankInfo.setEz(player.getEz());

        return tankInfo;
    }

    //重置玩家战斗属性
    private void resetPlayers() {
        //位置和旋转
        int count1 = 0;
        int count2 = 0;
        for (String id : playerIds.keySet()) {
            Player player = PlayerManager.getPlayer(id);
            if (player.camp == 1) {
                setBirthPos(player, count1);
                count1++;
            } else {
                setBirthPos(player, count2);
                count2++;
            }
            player.setHp(100);
        }
    }


    //开战
    public boolean startBattle() {
        if (!canStartBattle()) {
            return false;
        }
        //状态
        status = Status.FIGHT.getValue();
        //玩家战斗属性
        resetPlayers();
        //返回数据
        EnterBattlePacket msg = new EnterBattlePacket();
        msg.setMapId(1);
        msg.setTanks(new ArrayList<>());
        for (String id : playerIds.keySet()) {
            Player player = PlayerManager.getPlayer(id);
            msg.getTanks().add(playerToTankInfo(player));
        }
        broadcast(msg);
        return true;
    }

    //是否死亡
    public boolean isDie(Player player) {
        return player.hp <= 0;
    }

    //定时更新
    public void update() {
        //状态判断
        if (status != Status.FIGHT.getValue()) {
            return;
        }
        //时间判断
        if (System.currentTimeMillis() - lastJudgeTime < 10f) {
            return;
        }
        lastJudgeTime = System.currentTimeMillis();
        //胜负判断
        int winCamp = judgment();
        //尚未分出胜负
        if (winCamp == 0) {
            return;
        }
        //某一方胜利，结束战斗
        status = Status.PREPARE.getValue();
        //统计信息
        for (String id : playerIds.keySet()) {
            Player player = PlayerManager.getPlayer(id);
            if (player.camp == winCamp) {
                player.data.win++;
            } else {
                player.data.lost++;
            }
        }
        //发送Result
        BattleResultPacket msg = new BattleResultPacket();
        msg.setWinCamp(winCamp);
        broadcast(msg);
    }

    //胜负判断
    public int judgment() {
        //存活人数
        int count1 = 0;
        int count2 = 0;
        for (String id : playerIds.keySet()) {
            Player player = PlayerManager.getPlayer(id);
            if (!isDie(player)) {
                if (player.camp == 1) {
                    count1++;
                }
                if (player.camp == 2) {
                    count2++;
                }
            }
        }
        //判断
        if (count1 <= 0) {
            return 2;
        } else if (count2 <= 0) {
            return 1;
        }
        return 0;
    }

}


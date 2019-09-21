package com.fingard.xuesl.unity.tank.util;

import com.fingard.xuesl.unity.tank.bean.Room;
import com.fingard.xuesl.unity.tank.bean.RoomInfo;
import com.fingard.xuesl.unity.tank.protocol.GetRoomListPacket;
import com.fingard.xuesl.unity.tank.protocol.Packet;

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
public class RoomManager {
    //最大id
    private static int maxId = 1;
    //房间列表
    public static Map<Integer, Room> rooms = new ConcurrentHashMap<>();

    //创建房间
    public static Room addRoom() {
        maxId++;
        Room room = new Room();
        room.id = maxId;
        rooms.put(room.id, room);
        return room;
    }

    //删除房间
    public static Boolean removeRoom(int id) {
        rooms.remove(id);
        return true;
    }

    //获取房间
    public static Room getRoom(int id) {
        if (rooms.containsKey(id)) {
            return rooms.get(id);
        }
        return null;
    }

    //生成MsgGetRoomList协议
    public static Packet toMsg() {
        GetRoomListPacket packet = new GetRoomListPacket();
        packet.rooms = new ArrayList<>();
        //rooms
        for (Room room : rooms.values()) {
            RoomInfo roomInfo = new RoomInfo();
            //赋值
            roomInfo.setId(room.id);
            roomInfo.setCount(room.playerIds.size());
            roomInfo.setStatus(room.status);

            packet.rooms.add(roomInfo);
        }
        return packet;
    }
}

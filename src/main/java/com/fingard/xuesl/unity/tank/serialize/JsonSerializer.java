package com.fingard.xuesl.unity.tank.serialize;

import com.alibaba.fastjson.JSON;
import com.fingard.xuesl.unity.tank.protocol.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/10/010<br>
 * <br>
 */
public class JsonSerializer {
    private static final Map<String, Class<? extends Packet>> packetTypeMap;

    static {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put("MsgMove", MovePacket.class);
        packetTypeMap.put("MsgLogin", LoginPacket.class);
        packetTypeMap.put("MsgGetAchieve", GetAchievePacket.class);
        packetTypeMap.put("MsgCreateRoom", CreateRoomPacket.class);
        packetTypeMap.put("MsgEnterRoom", EnterRoomPacket.class);
        packetTypeMap.put("MsgGetRoomInfo", GetRoomInfoPacket.class);
        packetTypeMap.put("MsgGetRoomList", GetRoomListPacket.class);
        packetTypeMap.put("MsgLeaveRoom", LeaveRoomPacket.class);
    }

    public static byte[] serialize(Packet packet) {
        return JSON.toJSONBytes(packet);
    }

    public static Packet deserialize(String msgName, byte[] bytes) {
        if (!packetTypeMap.containsKey(msgName)) {
            return null;
        }
        Class<?> clazz = packetTypeMap.get(msgName);
        return JSON.parseObject(bytes, clazz);
    }
}

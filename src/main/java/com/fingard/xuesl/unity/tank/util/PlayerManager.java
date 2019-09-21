package com.fingard.xuesl.unity.tank.util;

import com.fingard.xuesl.unity.tank.bean.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/21/021<br>
 * <br>
 */
public class PlayerManager {

    //玩家列表
    public static Map<String, Player> players = new ConcurrentHashMap<>();

    //玩家是否在线
    public static Boolean isOnline(String id) {
        return players.containsKey(id);
    }

    //获取玩家
    public static Player getPlayer(String id) {
        if (players.containsKey(id)) {
            return players.get(id);
        }
        return null;
    }

    //添加玩家
    public static void addPlayer(String id, Player player) {
        players.put(id, player);
    }

    //删除玩家
    public static void removePlayer(String id) {
        players.remove(id);
    }
}

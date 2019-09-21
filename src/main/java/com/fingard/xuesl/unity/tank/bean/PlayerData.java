package com.fingard.xuesl.unity.tank.bean;

import lombok.Data;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/21/021<br>
 * <br>
 */
@Data
public class PlayerData {
    //金币
    public int coin = 0;
    //记事本
    public String text = "new text";
    //胜利数
    public int win = 0;
    //失败数
    public int lost = 0;
}

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
public class PlayerInfo {
    private String id = "";
    private int camp = 0;
    private int win = 0;
    private int lost = 0;
    private int isOwner = 0;
}

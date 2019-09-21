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
public class RoomInfo {
    //房间ID
    private int id = 0;
    //人数
    private int count = 0;
    //状态0-准备中 1-战斗中
    private int status = 0;
}

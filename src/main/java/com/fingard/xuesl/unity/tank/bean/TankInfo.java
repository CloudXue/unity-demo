package com.fingard.xuesl.unity.tank.bean;

import lombok.Data;
import lombok.ToString;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/22/022<br>
 * <br>
 */
@Data
@ToString
public class TankInfo {
    private String id = "";
    private int camp = 0;
    private int hp = 0;

    private float x = 0;
    private float y = 0;
    private float z = 0;
    private float ex = 0;
    private float ey = 0;
    private float ez = 0;
}

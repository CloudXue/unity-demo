package com.fingard.xuesl.unity.tank.codec;

import com.fingard.xuesl.unity.tank.protocol.Packet;
import com.fingard.xuesl.unity.tank.serialize.JsonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/10/010<br>
 * <br>
 */
@Slf4j
public class MsgDecoder extends MessageToMessageDecoder<ByteBuf> {
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        log.info("收到消息：" + byteBuf.toString(Charset.forName("UTF-8")));
        //解析数据包长度
//        byte[] packetLengthBytes = new byte[2];
//        byteBuf.readBytes(packetLengthBytes);
//        int packetLength = packetLengthBytes[1] << 8 | packetLengthBytes[0];
        int packetLength = byteBuf.readUnsignedShortLE();

        //解析协议名
//        byte[] nameLengthBytes = new byte[2];
//        byteBuf.readBytes(nameLengthBytes);
//        int nameLength = nameLengthBytes[1] << 8 | nameLengthBytes[0];
        int nameLength = byteBuf.readUnsignedShortLE();
        String name = byteBuf.readBytes(nameLength).toString(Charset.forName("UTF-8"));
        log.info("接收到的报文为：" + name);

        //解析消息体
        byte[] bodyBytes = new byte[packetLength - 2 - nameLength];
        byteBuf.readBytes(bodyBytes);
        Packet packet = JsonSerializer.deserialize(name, bodyBytes);
        list.add(packet);
    }
}

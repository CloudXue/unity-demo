package com.fingard.xuesl.unity.tank.codec;

import com.fingard.xuesl.unity.tank.protocol.Packet;
import com.fingard.xuesl.unity.tank.serialize.JsonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/9/10/010<br>
 * <br>
 */
public class MsgEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        byte[] protocolName = packet.getProtocolName().getBytes(Charset.forName("UTF-8"));
        byte[] msgBodyBytes = JsonSerializer.serialize(packet);
        int nameLength = protocolName.length;
        int msgLength = protocolName.length + msgBodyBytes.length + 2;
        byteBuf.writeByte(msgLength % 256);
        byteBuf.writeByte(msgLength / 256);
        byteBuf.writeByte(nameLength % 256);
        byteBuf.writeByte(nameLength / 256);
        byteBuf.writeBytes(protocolName);
        byteBuf.writeBytes(msgBodyBytes);
    }
}

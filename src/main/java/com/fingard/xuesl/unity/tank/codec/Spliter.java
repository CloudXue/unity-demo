package com.fingard.xuesl.unity.tank.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;

public class Spliter extends LengthFieldBasedFrameDecoder {
    private static final int LENGTH_FIELD_OFFSET = 0;
    private static final int LENGTH_FIELD_LENGTH = 2;

    public Spliter() {
        super(ByteOrder.LITTLE_ENDIAN, Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH,
                0, 0, true);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
//        if (in.getInt(in.readerIndex()) != PacketCodec.MAGIC_NUMBER) {
//            ctx.channel().close();
//            return null;
//        }
        return super.decode(ctx, in);
    }
}

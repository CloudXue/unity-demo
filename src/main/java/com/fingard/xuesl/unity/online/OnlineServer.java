package com.fingard.xuesl.unity.online;

import com.fingard.xuesl.unity.echo.EchoServerHandler;
import com.fingard.xuesl.unity.online.handler.EnterHandler;
import com.fingard.xuesl.unity.online.handler.ListHandler;
import com.fingard.xuesl.unity.online.handler.MoveHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 功能说明: <br>
 * 系统版本: 1.0 <br>
 * 开发人员: xuesl
 * 开发时间: 2019/8/30/030<br>
 * <br>
 */
public class OnlineServer {

    @SuppressWarnings("all")
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new StringEncoder());
                            p.addLast(new StringDecoder());
                            p.addLast(new EnterHandler());
                            p.addLast(new ListHandler());
                            p.addLast(new MoveHandler());
                        }
                    });

            // Start the server.
            ChannelFuture f = b.bind(8888).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

package com.cx.nettypro1.netty.heartBeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.NettyRuntime;

import java.util.concurrent.TimeUnit;

public class MyServer {

    public static void main(String[] args) {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();//这里默认创建得是自己cpu*2个线程
        int i = NettyRuntime.availableProcessors();
        System.out.println("自己cpu"+i);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();//i 个线程

        try {


            ServerBootstrap serverBootstrap=new ServerBootstrap();
            serverBootstrap.group(boosGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)) //增加日志
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    //加入netty 提供的IdleStateHandler
                    /**
                     * 说明 IdleStateHandler 是netty提供的处理空闲状态测处理器
                     * 1. readerldleTime  是表示多长时间没有读，就会发送一个心跳检测报检测是否连接
                     * 2. writerIdleTime   表示多长时间没有写，就会发送一个心跳检测包检测是否连接
                     * 3. long allIdleTime  表示多长时间没有读写，就会发送一个心跳检测包检测是否连接
                     *
                     */
                    //当IlestateEvent 触发后，就会传递给管道得下一个handler
                    //通过调用（触发下一个handler 得userEventTiggered），在该方法中去处理IdleStateHandler（读空闲，写空闲）
                    pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                    //加入一个对空闲检测进一步处理得handler（自定义）
                    pipeline.addLast(new MyServerHandler());

                }
            });
            ChannelFuture sync = serverBootstrap.bind(7888).sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}

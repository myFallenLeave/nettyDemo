package com.cx.nettypro1.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.NettyRuntime;

public class TestServer {

    public static void main(String[] args) {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();//这里默认创建得是自己cpu*2个线程
        int i = NettyRuntime.availableProcessors();
        System.out.println("自己cpu"+i);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //创建启动参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup, workGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用nio chanel 作为服务器通信
            //        .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到的线程数
              //      .childOption(ChannelOption.SO_KEEPALIVE, true)//保持活动连接状态
                    .childHandler(new TestServerInitiazer());

            ChannelFuture sync = serverBootstrap.bind(7000).sync();

            sync.channel().closeFuture().sync();
        } catch (Exception e){

        }finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();

        }
    }
}

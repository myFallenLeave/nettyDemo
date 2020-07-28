package com.cx.nettypro1.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.NettyRuntime;

public class GroupChatServer {


    private int port;//监听端口

    public GroupChatServer(int port){
        this.port=port;
    }

    //编写run接口
    public void run() throws  Exception{
        //创建两个线程组
        NioEventLoopGroup boosGroup = new NioEventLoopGroup(1);//这里默认创建得是自己cpu*2个线程
        int i = NettyRuntime.availableProcessors();
        System.out.println("自己cpu"+i);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();//i 个线程

        try {


        ServerBootstrap serverBootstrap=new ServerBootstrap();
        serverBootstrap.group(boosGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.SO_BACKLOG,128)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("decoder",new StringDecoder());
                        pipeline.addLast("encoder",new StringEncoder());
                        pipeline.addLast(new GroupChatServerHandler())
;
                    }
                });

        System.out.println("netty 服务启动");
        //绑定端口  并且同步处理，生成一个ChannelFuture
        ChannelFuture sync = serverBootstrap.bind(port).sync();
        sync.channel().closeFuture().sync();
        }finally {
        boosGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new GroupChatServer(6669).run();
    }

}

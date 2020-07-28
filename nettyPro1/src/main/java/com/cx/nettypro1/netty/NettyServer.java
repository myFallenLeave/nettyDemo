package com.cx.nettypro1.netty;

import com.cx.nettypro1.nio.Day2.NIOByteBufferPUTGET;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.NettyRuntime;

import java.security.spec.RSAOtherPrimeInfo;
import java.sql.SQLOutput;

public class NettyServer {

    public static void main(String[] args) throws InterruptedException {


        //创建BossGroup 和WorkGroup
        //bossGroup处理连接请求 真正的客户业务处理，都会交给WorkGroup
        //这两个都是无限循环
        //bossGroup 和 workerGroup 含有子线程得个数
        //  默认实际 cpu 核数*2
        NioEventLoopGroup boosGroup = new NioEventLoopGroup(1);//这里默认创建得是自己cpu*2个线程
        int i = NettyRuntime.availableProcessors();
        System.out.println("自己cpu"+i);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        //创建启动参数
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            //使用链式编程 进行设置
            serverBootstrap.group(boosGroup, workGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用nio chanel 作为服务器通信
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到的线程数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//保持活动连接状态
                    .handler(null)   //该handler 对应的是bossGroup ，child Handler 对应的是workGroup
                    .childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道测试对象（匿名对象）
                        //给pipeline添加处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //可以使用一个集合管理SocketChannel，再推送消息时，可以将业务加入到各个channel对应得NIOEventLoop得
                            //taskQueue 或者scheduleTaskQueue
                            System.out.println("客户socketchannel hascoed="+ch.hashCode());

                            ch.pipeline().addLast(new NettyServerHanler());
                        }
                    });   //给我们workeGroup 的EventLoop 对应的管道设置处理器


            System.out.println("server is ready");

            //绑定端口  并且同步处理，生成一个ChannelFuture
            ChannelFuture sync = serverBootstrap.bind(6668).sync();

            //给cf 注册监听器，监控我们关心的事件
            sync.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(sync.isSuccess()){
                        System.out.println("监听端口成功");
                    }else{
                        System.out.println("监听端口失败");
                    }
                }
            });

            //对关闭通道进行监听
            sync.channel().closeFuture().sync();

        } finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();

        }
    }
}
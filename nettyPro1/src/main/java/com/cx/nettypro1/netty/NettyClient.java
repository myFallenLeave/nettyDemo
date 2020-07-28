package com.cx.nettypro1.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) {

        //客户端需要一个事件循环组
        EventLoopGroup eventExecutors = new NioEventLoopGroup();

        //创建客户端启动对象
        //注意客户端使用得不是ServerBootStrap  而是BootStrap
        Bootstrap bootstrap=new Bootstrap();
        try {
        //设置相关参数
        bootstrap.group(eventExecutors)  //设置线程组
                .channel(NioSocketChannel.class)  //设置客户端通道得实现类
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new NettyClientHandler()) ;//加入自己的处理器
                    }
                });
        System.out.println("client ok");

        //启动客户端连接服务端

        //关于channelFuture 要分析，涉及netty得异步模型
        ChannelFuture cf = bootstrap.connect("127.0.0.1", 6668).sync();
        //给关闭通道进行监听

            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {

        }


    }
}

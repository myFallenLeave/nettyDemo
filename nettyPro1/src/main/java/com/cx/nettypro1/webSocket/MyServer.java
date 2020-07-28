package com.cx.nettypro1.webSocket;

import com.cx.nettypro1.netty.heartBeat.MyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.NettyRuntime;

import java.util.concurrent.TimeUnit;

public class MyServer {

    public static void main(String[] args) throws Exception{
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
                          //因为是基于http协议，使用http得编码解码器
                            pipeline.addLast(new HttpServerCodec());
                            //是以块方式写，添加chenkedWriteHandler处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            /**
                             * 说明 1.http 数据在传输过程中是分段，HttpObjectAggregator，是可以将多端聚合
                             *     2. 这就是为什么，当浏览器发送大量数据时，就会发生多次http请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8196));
                            /**
                             * 说明  websocket ，他得数据是以帧 形式传递
                             * 2.可以看到WebSockerFrame 下面有六个自类
                             * 3. 浏览器请求时 ws://localhost:7888/xxx    xxx表示请求的url
                             * 4. WebSocketServerProtocolHandler 核心功能是将 http 协议升级为ws 协议，保持长连接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            //自定义得handler，处理业务逻辑
                            pipeline.addLast(new MyTextWebSocketFrameHandler());

                        }
                    });
            ChannelFuture sync = serverBootstrap.bind(7888).sync();
            sync.channel().closeFuture().sync();
        } finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}

package com.cx.nettyc;

import com.cx.nettyc.nettyclient.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class NettyClient {

    private static  String host = null;
    private static  int port=0;
    private  static Channel channel;


//    public NettyClient(String host, int port) {
//        this.host = host;
//        this.port = port;
//    }

    public static void main (String[] args)throws Exception {
        host="127.0.0.1";
        port=8874;
   //     new NettyClient("127.0.0.1",8874).start();
        start();
      channel.writeAndFlush(Unpooled.copiedBuffer("send try !", CharsetUtil.UTF_8));


    }

    private static void start() throws Exception {

        /**
         * Netty用于接收客户端请求的线程池职责如下。
         * （1）接收客户端TCP连接，初始化Channel参数；
         * （2）将链路状态变更事件通知给ChannelPipeline
         */
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            //绑定端口
            ChannelFuture f = b.connect().sync();

            f.channel().closeFuture().sync();
            channel=b.connect().channel();
        } catch (Exception e) {
            group.shutdownGracefully().sync();
        }
    }
}

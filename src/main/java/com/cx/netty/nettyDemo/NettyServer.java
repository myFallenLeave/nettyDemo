package com.cx.netty.nettyDemo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Slf4j
@Configuration
@AllArgsConstructor
public class NettyServer  {


    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
  //  private Thread nserver;

public NettyServer(){}

//    public void bind(int port) throws Exception{
//
//        /**
//         * 配置服务端的NIO线程组
//         * NioEventLoopGroup 是用来处理I/O操作的Reactor线程组
//         * bossGroup：用来接收进来的连接，workerGroup：用来处理已经被接收的连接,进行socketChannel的网络读写，
//         * bossGroup接收到连接后就会把连接信息注册到workerGroup
//         * workerGroup的EventLoopGroup默认的线程数是CPU核数的二倍
//         */
//
////        EventLoopGroup loopGroup=new NioEventLoopGroup(1);
////        EventLoopGroup workerGroup=new NioEventLoopGroup();
////
////
////        ServerBootstrap serverBootstrap=new ServerBootstrap();
//
//       // serverBootstrap.group()
//
//
//    }

    /**
     * 关闭netty服务
     */
    public void closeServer(){
        log.info("shutdown Netty Server。。。。。。");
        if(channel!=null){
            channel.close();
        }
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("shutdown netty server Success!");
    }

    /**
     * 开启netty服务
     * @return
     */

    public ChannelFuture startServer(InetSocketAddress address){

        final EchoServerHandler serverHandler = new EchoServerHandler();

        ChannelFuture channelFuture=null;
        try {
            ServerBootstrap b=new ServerBootstrap();
            //主线程
            bossGroup = new NioEventLoopGroup(1);
            //bossGroup 和 workGroup 是怎么合作得
            //工作线程
            workerGroup = new NioEventLoopGroup();

            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(address)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //                            为监听客户端read/write事件的Channel添加用户自定义的ChannelHandler
                            socketChannel.pipeline().addLast(serverHandler);
                        }
                    });

            channelFuture= b.bind().sync();
        }catch (Exception e){
            System.err.println("netty启动失败");
            e.printStackTrace();
        }finally {
            if (channelFuture!=null&&channelFuture.isSuccess()){
                System.out.println("netty 正在监听"+address.getHostName()+"端口"+address.getPort()+",等待连接");
            }else{
                System.out.println("netty 启动失败！");
            }
        }

        return channelFuture;
    }

    /**/



}

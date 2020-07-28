package com.cx.netty;

import com.cx.netty.nettyDemo.NettyServer;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;

@SpringBootApplication
public class NettyApplication  {

    @Autowired
    private NettyServer nettyServer;
    public static void main(String[] args) {
        SpringApplication.run(NettyApplication.class, args);
        try {
            run();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 在这里启动netty服务
     * @throws Exception
     */
   // @Override
//    public static void main(String[] args) throws Exception {
//        run();
//    }
    public static  void run() throws Exception {

        InetSocketAddress inetSocketAddress=new InetSocketAddress(8874);
        NettyServer nettyServer=new NettyServer();
        //启动·netty服务
        ChannelFuture channelFuture = nettyServer.startServer(inetSocketAddress);

        // 钩子方法，关闭服务器
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                nettyServer.closeServer()
        ));
        //服务端管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程
        channelFuture.channel().closeFuture().syncUninterruptibly();

    }
}

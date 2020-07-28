package com.cx.nettypro1.netty.groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.SctpChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class GroupChatClient {

    private final String host;
    private final int port;
    public GroupChatClient(String host,int port){
        this.host=host;
        this.port=port;
    }

    public void run() throws InterruptedException {

        EventLoopGroup eventExecutors=new NioEventLoopGroup();

        try {


            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("decoder", new StringDecoder());
                            pipeline.addLast("encoder", new StringEncoder());
                            pipeline.addLast(new GroupChatClientHandler());
                        }
                    });

            ChannelFuture sync = bootstrap.connect(host, port).sync();
            //得到channel
            Channel channel = sync.channel();
            System.out.println("------"+channel.localAddress()+".........");
            //客户端需要输入信息，创建一个扫描器
            Scanner scanner=new Scanner(System.in);
            while (scanner.hasNext()){
                String msg=scanner.nextLine();
                //通过channel 发送给服务器
                channel.writeAndFlush(msg+"\n");
            }


        }finally {
            eventExecutors.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        new GroupChatClient("127.0.0.1",6669).run();
    }
}

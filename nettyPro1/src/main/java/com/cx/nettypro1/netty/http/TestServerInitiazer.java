package com.cx.nettypro1.netty.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


/**
 * 写入哪个channel
 * @author 86131
 */
public class TestServerInitiazer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器


        //得到管道   这是个双向链接
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个netty 提供的http ServerCodec codec=》【coder - decoder】
        //HttpServerCodec 说明
        //1. HttpServerCodec 是netty提供的处理Http的编码解码器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        //增加一个自定义的handler
        pipeline.addLast("MyTestHttpServerHandler",new TestHttpServerHandler());


    }
}

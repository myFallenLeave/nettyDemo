package com.cx.nettypro1.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

        byte[] buff=new byte[msg.readableBytes()];
        msg.readBytes(buff);

        //将buffer 转成字符串
        String messages = new String(buff, CharsetUtil.UTF_8);
        System.out.println("服务器接受得到的数据"+messages);
        System.out.println("服务器接收到的消息量"+(++this.count));

        //服务器回送数据给客户端，回送一个随机ID
        ByteBuf buf = Unpooled.copiedBuffer(UUID.randomUUID().toString(), CharsetUtil.UTF_8);
        ctx.writeAndFlush(buf);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getCause());

        ctx.close();
    }
}

package com.cx.nettypro1.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {


        int len = msg.getLen();
        byte[] content = msg.getContent();
        System.out.println("服务端接收信息如下");

        System.out.println("长度"+len);
        System.out.println("内容"+new String(content,CharsetUtil.UTF_8));

        System.out.println("服务器接收到消息包数量"+(++this.count));

        //服务器回送数据给客户端，回送一个随机ID
        String re=UUID.randomUUID().toString();
        byte[] bytes = re.getBytes();
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setContent(bytes);
        messageProtocol.setLen(bytes.length);
        ctx.writeAndFlush(messageProtocol);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getCause());

        ctx.close();
    }
}

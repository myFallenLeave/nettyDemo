package com.cx.nettypro1.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

   private int cout;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {

        byte[] content = msg.getContent();
        int len = msg.getLen();
        System.out.println("客户端接收到的消息如下");
        System.out.println("内容="+new String(content,CharsetUtil.UTF_8));
        System.out.println("长度"+len);
        System.out.println("客户端接收到的数量"+(++this.cout));


    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      //使用客户端发送10条数据 hello，server
        for (int i=0;i<10;++i){
            String message="今天天气冷  " + i+" ";
            byte[] bytes = message.getBytes(CharsetUtil.UTF_8);
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(bytes.length);
            messageProtocol.setContent(bytes);

            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

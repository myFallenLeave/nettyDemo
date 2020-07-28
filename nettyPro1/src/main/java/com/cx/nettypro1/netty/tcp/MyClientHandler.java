package com.cx.nettypro1.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;

public class MyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

   private int cout;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] bt=new byte[msg.readableBytes()];

        msg.readBytes(bt);
        String resive = new String(bt, CharsetUtil.UTF_8);
        System.out.println("接收到的数据"+resive);
        System.out.println("接收到"+(++this.cout));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      //使用客户端发送10条数据 hello，server
        for (int i=0;i<10;++i){

            ByteBuf buf = Unpooled.copiedBuffer("hello.server" + i+" ", CharsetUtil.UTF_8);
            ctx.writeAndFlush(buf);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

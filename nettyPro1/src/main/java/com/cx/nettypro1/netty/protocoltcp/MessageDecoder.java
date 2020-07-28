package com.cx.nettypro1.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import javax.print.DocFlavor;
import java.util.List;

/**
 * 解码
 */
public class MessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("解码被调用");
        //需要将而精致字节码转换城 messageProtocol 数据包
        int length=in.readInt();
        byte[] content=new byte[length];
        in.readBytes(content);

        //封装成MessageProtocol 对象 放入out，传入下一个hanler 业务处理
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(length);
        messageProtocol.setContent(content);
        out.add(messageProtocol);
    }
}

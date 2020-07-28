package com.cx.nettypro1.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class NettyByteBuf02 {

    public static void main(String[] args) {
      //创建byteBuf
        ByteBuf buf = Unpooled.copiedBuffer("hello,s", CharsetUtil.UTF_8);

        //使用相关的方法
        if(buf.hasArray()){//true
            byte[] content = buf.array();
            //将content转成字符串
            System.out.println(new String(content,CharsetUtil.UTF_8));
            System.out.println("byteBuf"+buf);
            System.out.println("readerIndex"+buf.readerIndex());
            System.out.println(buf.writerIndex()
            );
            System.out.println(buf.arrayOffset());

            //读取一个后面readableBytes会有变化
        //    System.out.println(buf.readByte());
            int len=buf.readableBytes();//可读取的字节数
            System.out.println(len);

            for (int i=0;i<len;i++
                 ) {
                System.out.println((char)buf.getByte(i));

            }
            System.out.println(buf.getCharSequence(0,4,CharsetUtil.UTF_8));

        }

    }
}

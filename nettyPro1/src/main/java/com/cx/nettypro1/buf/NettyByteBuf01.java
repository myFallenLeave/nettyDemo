package com.cx.nettypro1.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {

    public static void main(String[] args) {
        //创建一个ByteBuf
        //说明
        //1.创建对象，该对象包含一个数组arr ，是一个byte[0]
        //2. 在netty得buffer种，不需要filp 进行翻转
        //  （因为在底层维护了 readerindex 和writeIndex）
        //3. 通过readerindex 和writerIndex 和capacity 分成了三段
        //0----readerindex  已经读取得区域
        //readerindex -writerindex 可以读得区域
        //writerIndex--capacity 可以写得区域
        ByteBuf buf= Unpooled.buffer(10);

        for (int i=0;i<10;i++){
            buf.writeByte(i);
        }
        //输出
        for (int i=0;i<buf.capacity();i++){
            System.out.println(buf.getByte(i));
        }

        for(int i=0;i<buf.capacity();i++){
            System.out.println(buf.readByte());
        }

    }
}

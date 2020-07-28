package com.cx.nettypro1.nio.Day2;

import java.nio.ByteBuffer;

public class NIOByteBufferPUTGET {

    public static void main(String[] args) {

        //buffer 对应的类型要对应上

        ByteBuffer buffer=ByteBuffer.allocate(18);
        buffer.putInt(100);
        buffer.putLong(9);
        buffer.putInt(22);
        buffer.putShort((short)2);

        buffer.flip();

        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getInt());
     //   System.out.println(buffer.getShort());


        buffer.clear();
        buffer.putInt(1);
        System.out.println("dddddddd"+buffer.getInt());
//        //得到一个只读得Buffer
//        ByteBuffer byteBuffer = buffer.asReadOnlyBuffer();
    }
}

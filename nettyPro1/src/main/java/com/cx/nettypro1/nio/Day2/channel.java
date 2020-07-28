package com.cx.nettypro1.nio.Day2;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

public class channel {

    /**
     * 用通道写如文件
     * @param args
     */
    public static void main(String[] args) throws Exception{
        String str ="hello";
        //创建输出流 nio 是对bio得包装
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\filetest01.txt");
        //通过fileOutputStream h获取FileChannel;
        //这个fileChannel 真是类型是FileChannelImpl
        FileChannel channel = fileOutputStream.getChannel();

        //创建一个缓冲区，byteBuffer
        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
        //将str 放入byteBuffer
        byteBuffer.put(str.getBytes());
        //对byte进行flip
        byteBuffer.flip();
        //将不要teBuffer 数据写入到fileChannel
        channel.write(byteBuffer);

        fileOutputStream.close();

    }
}

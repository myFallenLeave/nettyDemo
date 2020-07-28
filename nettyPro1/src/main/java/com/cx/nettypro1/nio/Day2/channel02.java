package com.cx.nettypro1.nio.Day2;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class channel02 {

    public static void main(String[] args) throws  Exception{
        //创建文件输入流

        FileInputStream fileInputStream = new FileInputStream(new File("d:\\filetest01.txt"));
        FileChannel channel = fileInputStream.getChannel();

        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);

        channel.read(byteBuffer);

        //将字节转成字符串
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();


    }
}

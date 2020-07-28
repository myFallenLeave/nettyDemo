package com.cx.nettypro1.nio.Day2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class channel03 {

    public static void main(String[] args) throws  Exception{
        //创建文件输入流

        FileInputStream fileInputStream = new FileInputStream(new File("1.txt"));
        FileChannel channel01 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel channel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer=ByteBuffer.allocate(20);
        while (true){

            //这里要有一个重要的操作，重置数据
        //    byteBuffer.clear();
            int read = channel01.read(byteBuffer);
            System.out.println("read"+read);
            if(read==-1){
                break;
            }
            byteBuffer.flip();
            int write = channel02.write(byteBuffer);
       //     byteBuffer.flip();
        }



        //将字节转成字符串
   //     System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
        fileOutputStream.close();

    }
}

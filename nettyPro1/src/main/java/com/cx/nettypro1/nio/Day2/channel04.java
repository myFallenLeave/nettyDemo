package com.cx.nettypro1.nio.Day2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class channel04 {


    public static void main(String[] args) throws Exception{
        FileInputStream fileInputStream=new FileInputStream("D:\\image\\new\\1.jpg");
        FileOutputStream fileOutputStream=new FileOutputStream("D:\\image\\new\\2.jpg");

        FileChannel source = fileInputStream.getChannel();
        FileChannel destch=fileOutputStream.getChannel();

        destch.transferFrom(source,0,source.size());

        fileInputStream.close();
        fileOutputStream.close();
    }
}

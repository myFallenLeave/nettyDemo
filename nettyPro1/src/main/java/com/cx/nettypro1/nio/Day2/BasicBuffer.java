package com.cx.nettypro1.nio.Day2;

import java.nio.IntBuffer;

public class BasicBuffer {
//    public static void main(String[] args) {
//        //举例说明Buffer得使用（简单说明）
//        //创建一个BUffer,大小为5，即可以存放5个大小
//        IntBuffer intBuffer=IntBuffer.allocate(5);
//
//        //向buffer 存放数据
//        intBuffer.put(10);
//        intBuffer.put(11);
//        intBuffer.put(12);
//        intBuffer.put(13);
//        intBuffer.put(14);
//
//        //从buffer里面读取数据
//        //将buffer转换，读写切换
//        intBuffer.flip();
//        //设置position位置
//        intBuffer.position(1);
//        while (intBuffer.hasRemaining()){
//            //get 里面有有维护索引 get过后 指针会后移
//            System.out.println(intBuffer.get());
//        }
//
//    }
    public static void main(String[] args) {

        int a=10;
        a=a+10000/100;
        System.out.println(a);
    }
}

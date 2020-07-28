package com.cx.nettypro1.nio.Day2;


import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 说明  MapperByteBuffer  可让文件直接在内存修改，操作系统不需要拷贝一次（操作系统级别的修改）
 */
public class MapperBufferTest {

    public static void main(String[] args) throws Exception{
        RandomAccessFile rw = new RandomAccessFile("1.txt", "rw");

        //获取对应的通道
        FileChannel channel=rw.getChannel();

        /**
         * 参数1：FileChannel.MapMode.READ_WRITE  使用读写模式
         * 参数2：  0： 可以修改得起始位置
         * 参数3： 5 是映射到文件得大小(不是索引)，即是将1.txt得多少自己映射到内存
         * 可以直接修改的范围是0-5
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.put(0,(byte)'H');
        map.put(3,(byte)'9');
        rw.close();

    }
}

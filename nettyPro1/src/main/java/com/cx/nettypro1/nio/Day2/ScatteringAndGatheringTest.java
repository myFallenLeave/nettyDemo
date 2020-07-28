package com.cx.nettypro1.nio.Day2;


import org.w3c.dom.ls.LSOutput;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering：将数据写入buffer，采用buffer数组，一次写入（分散）
 * Gathering 从buffer读取数据，采用buffer数组，依次读（聚合）
 */
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws Exception{
        //使用ServerSocketChannel 和SocketChannel网络
        ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();

        InetSocketAddress inetSocketAddress=new InetSocketAddress(7000);

        //绑定端口到socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建buffer数组
        ByteBuffer[] byteBuffers=new ByteBuffer[2];

        //数据发送为先填满第一个
        byteBuffers[0]=ByteBuffer.allocate(5);
        byteBuffers[1]=ByteBuffer.allocate(3);

        //等客户端连接
        SocketChannel socketChannel=serverSocketChannel.accept();

        int message=8;//假定客户端接受8个字节
        //循环读取
        while (true){
            int byteRead=0;
            while (byteRead<message){
                long l=socketChannel.read(byteBuffers);
                byteRead+=1;//累计读取得字节数
                System.out.println("byteRead:"+byteRead);

                Arrays.asList(byteBuffers).stream().map(buffer->"position"+buffer.position()+",limit"+buffer.limit()).forEach(System.out::println);

            }
            //将所有的buffer进行flip
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());

            //将数据读出显示到客户端
            long byteWrite=0;
            while (byteWrite<message){
                socketChannel.write(byteBuffers);//
                byteWrite+=1;

            }
            //将所有buffer复位
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());
            System.out.println("ByteRead:="+byteRead+"byteWrite:"+byteWrite);



        }


    }
}

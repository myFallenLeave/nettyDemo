package com.cx.nettypro1.nio.Day3;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {


    public static void main(String[] args) throws Exception{
        //创建ServerSocketChannel-->ServerSocket
        ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();

        //得到Selecor对象
        Selector selector=Selector.open();


        //绑定一个端口6666，在服务器监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel 注册到selector 关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //
        System.out.println("客户端 注册后的selectKey 数量+"+selector.keys().size());//1

        //循环等待客户端连接
        while (true){
            //这里等待1s，如果没有事建发生就返回
            if(selector.select(1000)==0){
                //没有事情发生
                System.out.println("服务器等待1s");
                continue;
            }
            //如果返回得》0,就获取到相关的selectionKey集合
            //1.如果返回》0，表示已经获取到关注的事件
            //2.通过selector.selectedKeys() 返回关注事件的集合
            //3. 通过selectionKeys获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            System.out.println("只对事件发生会产生数据："+selectionKeys.size());
            //遍历集合，使用迭代器遍历
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                //获取到SelectionKey
                SelectionKey next = iterator.next();
                //根据key，对应的通道发生的事件做相应的处理
                if(next.isAcceptable()){//OP_Accpet,有新的客户端连接
                    //该客户端生成一个SocketChannel
                    SocketChannel socketChannel=serverSocketChannel.accept();
                    // 将socketChannel 注册到selector，关注事件为OP_READ,同时给socketChannel

                    //不加上这个 会报错 java.nio.channels.IllegalBlockingModeException
                    //将socketchannel 设置成非阻塞
                    socketChannel.configureBlocking(false);

                    System.out.println("客户端连接成功，生成了一个socketChannel "+socketChannel.hashCode());
                    //关联一个Buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                    System.out.println("客户端 ..注册后的selectKey 数量+"+selector.keys().size());//1
                }
                if(next.isReadable()){//发生OP_READ
                    //通过KEY 反向获取到对应channel
                    SocketChannel channel=(SocketChannel)next.channel();
                    //获取到该channel关联得buffer
                    ByteBuffer byteBuffer=(ByteBuffer)next.attachment();
                    channel.read(byteBuffer);
                    System.out.println("form 客户端"+new String(byteBuffer.array()));
                }
                //手动从集合中移动当前selectionkey
                iterator.remove();
            }


        }

    }
}

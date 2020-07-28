package com.cx.nettypro1.nio.Day4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.sql.SQLOutput;
import java.util.Iterator;

public class GroupChatServer {

    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final  int PORT=6667;
    //构造器初始化
    public GroupChatServer (){

        try {
            selector=Selector.open();
            listenChannel=ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞
            listenChannel.configureBlocking(false);
            //将该listenChannel 注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //监听
    public void listen(){


        try {
            while (true){
                //等待2s
                int count=selector.select(2000);
                if(count>0){//优势件处理
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();

                        if(key.isAcceptable()){//监听连接事件
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            //将sc 注册到selector上面
                            sc.register(selector,SelectionKey.OP_READ);
                            //给出提示
                            System.out.println(sc.getRemoteAddress()+"上线");


                        }
                        if(key.isReadable()){//读取事件,通道发生发生可读
                            //处理读
                                readData(key);
                        }
                        //当前的key 删除，防指重复处理
                        iterator.remove();
                    }



                }else {
                    System.out.println("等待...");
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //读取客户端消息
    public void  readData(SelectionKey key)  {
        //定义一个SocketChannel
        SocketChannel channel=null;

        try {
            //得到channel
             channel = (SocketChannel)key.channel();
             //创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = channel.read(buffer);
            //根据count的值做处理
            if(read>0){
                //将缓冲区数据转发
                String msg = new String(buffer.array());
                System.out.println("from 客户端:"+msg);
                //像其他客户端转发消息
                sendInfoToOtherClients(msg,channel);
            }

        }catch (IOException e){
            e.printStackTrace();
            try {
                System.out.println(channel.getRemoteAddress()+"离线");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            }catch (IOException es){
                es.printStackTrace();
            }

        }

    }

    public void sendInfoToOtherClients(String msg,SocketChannel self)throws     IOException{
        System.out.println("服务器转发消息。。。");
        System.out.println("服务器转发数据给客户线程"+ThreadLocal.class);
        //遍历 所有注册到selector 上面的socketChannel
        for (SelectionKey key:selector.keys()){
            //通过可以渠道对应的 SocketChannel
            Channel channel = key.channel();
            //排除自己
            if(channel instanceof SocketChannel&&channel!=self){
                //转型
                SocketChannel dest=(SocketChannel)channel;
                //将msg 存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer 数据写入到通道中
                dest.write(buffer);

            }
        }

    }

    public static void main(String[] args) {
        //创建服务器对象
        new GroupChatServer().listen();

    }
}

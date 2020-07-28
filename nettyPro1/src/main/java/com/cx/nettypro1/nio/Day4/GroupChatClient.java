package com.cx.nettypro1.nio.Day4;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class GroupChatClient {

    //定义
    private final String HOST="127.0.0.1";//服务器IP

    private final int PORT=6667;//服务器端口

    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient()throws IOException {
        selector=Selector.open();
        socketChannel=SocketChannel.open(new InetSocketAddress(HOST,PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //将channel 注册到
        socketChannel.register(selector, SelectionKey.OP_READ);
        //
        username=socketChannel.getLocalAddress().toString().substring(1);

        System.out.println(username+"is ok");
    }

    //向服务器发送消息
    public void  sendInfo(String info){
        info=username+"说:"+info;

        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    
    public void readInfo(){
        try {
            int select = selector.select();
            if(select>0){//有可用的通道
                Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();
                while (selectionKeys.hasNext()){
                  SelectionKey key=  selectionKeys.next();
                  if(key.isReadable()){
                      //得到相关的通道
                      SocketChannel socketChannel=(SocketChannel)key.channel();
                      //得到BUffer
                      ByteBuffer buffer=ByteBuffer.allocate(1024);
                      //读取
                      socketChannel.read(buffer);
                      //转成
                      String mg = new String(buffer.array());
                      System.out.println(mg);
                  }
                    System.out.println("客户端正在做删除keys");
                  selectionKeys.remove();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        GroupChatClient groupChatClient = new GroupChatClient();


        new Thread(){
            @Override
            public void run() {
                while (true){
                    groupChatClient.readInfo();
                    try {
                        Thread.sleep(3000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        //发送数据
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){
            String s = scanner.nextLine();
            groupChatClient.sendInfo(s);
        }

    }
}

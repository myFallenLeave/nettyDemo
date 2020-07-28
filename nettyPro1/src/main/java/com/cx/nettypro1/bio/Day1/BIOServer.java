package com.cx.nettypro1.bio.Day1;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {

    public static void main(String[] args) throws IOException {

        //线程池机制

        //思路
        //1.创建有一个线程池
        //2.如果客户端连接，创建一个线程，与之通信


        ExecutorService newCachedThread= Executors.newCachedThreadPool();

        ServerSocket serverSocket=new ServerSocket(6666);

        System.out.println("服务启动了");
        while (true){
            System.out.println("线程id"+Thread.currentThread().getId()
                    +"线程名称"+Thread.currentThread().getName());
            //bio 阻塞
            System.out.println("等待连接。。。。");
            //监听  等待客户端连接
            final Socket socket=serverSocket.accept();

            System.out.println("连接到一个用户了");

            //就创建一个线程，与之通讯
            newCachedThread.execute(()->{
                    handler(socket);
            });

        }

    }
    //编写一个handler方法，和客户端童询
    public static  void handler(Socket socket){

        try {
            System.out.println("线程id"+Thread.currentThread().getId()
            +"线程名称"+Thread.currentThread().getName());
            byte[] bytes=new byte[1024];
            InputStream inputStream=socket.getInputStream();
            //循环读取客户发送的数据
            while (true){

                //连接建立后 会一直等待read，造成线程得浪费
                System.out.println("read.....");
                int read=inputStream.read(bytes);
                if(read!=-1){
                    System.out.println(new String(bytes,0,read));//输出客户端发送的数据
                }else {
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("关闭服务");
            try {
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}

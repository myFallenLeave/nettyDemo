package com.cx.nettypro1.netty.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 1. 我们自定义的yigeHandler 需要继续netty 绑定好的某个HandlerAdapter
 * 2. 这时我们自定义的一个Handler ，才能称Handler
 */
public class NettyServerHanler extends ChannelInboundHandlerAdapter {

    //读取数据实际（这里可以读取客户端发送的消息）

    /**
     * 1. ChannelHandlerContex ctx 上下文对象 含有管道 ，通道
     * 2. Object msg  就是客户端发送得消息  默认Object
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

//        System.out.println("服务器读取线程"+Thread.currentThread().getName());
//        System.out.println("server ctx=" + ctx);
//        System.out.println("channel 和pipeline得关系");
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline();//本质是双向链表，出栈，入栈问题
//
//        //将msg 转成一个 BYteBuffer
//        //ByteBuf  netty提供  不是NIO提供
//        //ByteBuf 性能更高
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端发送的消息:" + buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址" + ctx.channel().remoteAddress());

//        //比如这里有一个非常耗时得业务-->异步执行--->提交该channel 对应得NIOEventLoop
//        Thread.sleep(10*1000);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("duddududududdu",CharsetUtil.UTF_8));

        /**
         * 解决方案1  用户程序自定义得普通任务
         */
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("喵1",CharsetUtil.UTF_8));
                }catch (Exception e){
                    System.out.println("发生异常"+e.getMessage());
                }
            }
        });
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //目前实在一个线程里面  会加上前面睡得10秒钟 在执行
                    Thread.sleep(30*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("喵2",CharsetUtil.UTF_8));
                }catch (Exception e){
                    System.out.println("发生异常"+e.getMessage());
                }
            }
        });
        System.out.println("go on....");

        //用户自定义定时任务---》该任务是提交到 scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    //目前实在一个线程里面
                    Thread.sleep(5*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("喵三。。。。。。",CharsetUtil.UTF_8));
                }catch (Exception e){
                    System.out.println("发生异常"+e.getMessage());
                }
            }
        },5, TimeUnit.SECONDS);


    }

    //读取数据完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //write 和flush 方法
        //将数据写入到缓存并刷新
        //一般来讲，我们对这个发送数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端", CharsetUtil.UTF_8));

    }

    //处理异常，一般是关闭通道

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();

    }
}
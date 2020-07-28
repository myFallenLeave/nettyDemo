package com.cx.nettypro1.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    //定义一个chanchan'组，管理所有的channel
    //GlobalEventExecutor.INSTANCE 是全局得事件执行器，是一个单例
    private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //使用hashMap 管理
    public static Map<String,Channel> channels=new HashMap<>();

    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //handlerAdded 表示链接简历，一旦链接，第一个被执行
    //将当前channel加入到channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将客户加入聊天的信息推送给其他在线客户端

        /**
         * 将所有得channelGroup 种将所有的channel 遍历，并发送消息，我们不需要自己遍历
         */
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+"加入聊天");
        channelGroup.add(channel);
        channels.put("id",channel);
    }
    //表示channel 处于活动状态，提示某某上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println(ctx.channel().remoteAddress()+"上线了");
    }

    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        //这时我们遍历channel Group ，根据不同情况，回送不同消息
        channelGroup.forEach(ch->{
            if(channel!=ch){
                ch.writeAndFlush("[客户]"+channel.remoteAddress()+"发送了信息:"+msg+"\n");
            }else {
                ch.writeAndFlush("[自己]发送了消息"+msg+"\n");
            }

        });




    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    //离线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"离线了");
        }

        //断开连接  将xx客户离开信息推送给当前在线得客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
       channelGroup.writeAndFlush(ctx.channel().remoteAddress()+"离开了")

               ;
        System.out.println(channelGroup.size());
       //不需要这部分  这个会自己移除
       //channelGroup.remove(ctx.channel());

    }
}

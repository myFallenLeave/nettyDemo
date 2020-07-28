package com.cx.nettypro1.netty.inboundandOutBoundhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {

        //读到之后就回送消息
        System.out.println("actice");

    }

    //重写channelActive 发送数据

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler  发送数据");
        /**
         * 分析
         * 再写入的时候，如果是我们定义的handler 应该会执行下一个字符赚的handler但是实力不会执行
         * 1.因为源码里面的write 里面加入如果是该类型才会执行
         * 2.如果不是该类型就不会执行
         *
         */

        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));
    //    ctx.writeAndFlush(122333L);

    }
}

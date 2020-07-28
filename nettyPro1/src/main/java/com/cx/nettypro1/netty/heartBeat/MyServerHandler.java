package com.cx.nettypro1.netty.heartBeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            //将evt 向下转型IdleStateEvent
            IdleStateEvent event = (IdleStateEvent) evt;

            String eventType=null;
            switch (event.state()){
                case READER_IDLE:
                    eventType="读空闲";
                    break;
                case WRITER_IDLE:
                    eventType="写空闲";
                    break;
                case ALL_IDLE:
                    eventType="读写空闲";
                    break;
            }

            System.out.println(ctx.channel().remoteAddress()+"--超时时间--"+eventType);

            System.out.println("服务器已经做出对应处理");
            //发生空闲关闭通道
            ctx.channel().close();

        }

    }
}

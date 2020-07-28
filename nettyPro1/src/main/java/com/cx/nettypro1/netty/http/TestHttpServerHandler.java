package com.cx.nettypro1.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;


/**
 * 1. SimpleChannelInboundHandler 是ChannelInboundHandlerAdapter
 * 2. HttpObject 客户端和服务器相互童询的数据封装成 HttpObject
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {


        System.out.println("对应的channel是"+ctx.channel()+"对应的pipeline是"+ctx.pipeline()+"通过pipe获取channel 获取"+ctx.pipeline().channel());
        System.out.println("handler"+ctx.handler());
        //判断msg 是不是httprequest请求
        if (msg instanceof HttpRequest){

            System.out.println("ctx 正式类型"+ctx.getClass());

            System.out.println("pipeline has coed"+ctx.pipeline().hashCode()+"TestHttpServerHandler hash="+this.hashCode()  );
            System.out.println("msg 类型="+msg.getClass());
            System.out.println("客户端地址"+ctx.channel().remoteAddress());

            //获取到的msg
            HttpRequest httpRequest=(HttpRequest) msg;
            //获取uri
            URI uri = new URI(httpRequest.uri());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了 favicon  不做响应");
                return;
            }

            //回复信息给浏览器【http协议】
            ByteBuf content= Unpooled.copiedBuffer("hello，我是服务器", CharsetUtil.UTF_8);
            //构造一个http响应，即httpresponse
            DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK,content);
            defaultFullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            defaultFullHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
            // 将构建好的 response 返回
            ctx.writeAndFlush(defaultFullHttpResponse);
        }
    }

}

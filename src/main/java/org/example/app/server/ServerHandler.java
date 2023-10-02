package org.example.app.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    // Так как моя програма не оповещает всех пользователей, список пользователей не нужен

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        System.out.println("Client joined - " + ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        // Я сделал, чтобы сервер отправлял сообщение в
        // ответ только тому пользователю который ему написал
        System.out.println("Message received: " + msg);
        ctx.channel().writeAndFlush("Hello " + msg + '\n');
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Closing connection for client - " + ctx);
        ctx.close();
    }

}

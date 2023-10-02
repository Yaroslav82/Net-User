package org.example.app.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    // Словарь пользовательских каналов и кол-ва сообщений отправленных от них к серверу
    static final Map<Channel, AtomicInteger> channels = new HashMap<>();

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        System.out.println("User joined - " + ctx);
        channels.put(ctx.channel(), new AtomicInteger());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        // Я сделал, чтобы сервер отправлял сообщение в
        // ответ только тому пользователю который ему написал
        // Также если пользователь напишет quit сервер отключит его
        System.out.println("Message received: " + msg);
        Channel channel = ctx.channel();
        int msgsAmount = channels.get(channel).incrementAndGet();

        if (msg.equals("quit") || msgsAmount == 15) {
            ctx.disconnect();
            channels.remove(channel);
            System.out.println("User disconnected: " + ctx);
        } else if (msgsAmount == 14) {
            channel.writeAndFlush("One more message and I'll disconnect you!");
        } else if (msgsAmount >= 10) {
            channel.writeAndFlush("Stop texting to me! I told you the conversation won't work");
        } else {
            channel.writeAndFlush("Hello User! I'm not ChatGpt yet " +
                    "so there won't be an interesting conversation :(");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Closing connection for user - " + ctx);
        ctx.close();
    }
}

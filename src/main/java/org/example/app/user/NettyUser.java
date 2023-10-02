package org.example.app.user;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NettyUser {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8001;

    private final EventLoopGroup group = new NioEventLoopGroup();

    private static final Logger LOGGER = Logger.getLogger(NettyUser.class.getName());

    public static void main(String[] args) {
        NettyUser user = new NettyUser();
        user.connect();
    }

    public void connect() {
        try {
            Bootstrap b = configure();

            ChannelFuture f = b.connect(HOST, PORT).sync();
            Channel channel = f.sync().channel();
            Scanner scanner = new Scanner(System.in);
            System.out.print("Write message to server: ");

            for (int i = 0; i < 15; i++) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("quit")) {
                    channel.writeAndFlush(input.toLowerCase());
                    break;
                }
                channel.writeAndFlush(input);
            }

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, "Connection was interrupted");
        } finally {
            releaseResources();
        }
    }

    private Bootstrap configure() {
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new UserInitializer());
        return b;
    }

    private void releaseResources() {
        group.shutdownGracefully();
    }
}

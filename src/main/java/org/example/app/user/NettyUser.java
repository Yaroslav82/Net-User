package org.example.app.user;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyUser {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8001;

    private final EventLoopGroup group = new NioEventLoopGroup();

    public static void main(String[] args) throws InterruptedException {
        NettyUser user = new NettyUser();
        user.connect(HOST, PORT);
    }

    public void connect(String host, int port) throws InterruptedException {
        try {
            Bootstrap b = configure();

            ChannelFuture f = b.connect(host, port).sync();

            String input = "Tom";
            Channel channel = f.sync().channel();
            channel.writeAndFlush(input);
            channel.flush();

            f.channel().closeFuture().sync();
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

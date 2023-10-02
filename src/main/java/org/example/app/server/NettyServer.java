package org.example.app.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    private static final int PORT = 8001;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    public static void main(String[] args) throws InterruptedException {
        NettyServer server = new NettyServer();
        server.start(PORT);
    }

    public void start(int port) throws InterruptedException {
        try {
            ServerBootstrap b = configure();

            ChannelFuture f = b.bind(port).sync();
            System.out.println("Server started and waiting for users...");

            f.channel().closeFuture().sync();
        } finally {
            releaseResources();
        }
    }

    private ServerBootstrap configure() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerInitializer());
        return b;
    }

    private void releaseResources() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}

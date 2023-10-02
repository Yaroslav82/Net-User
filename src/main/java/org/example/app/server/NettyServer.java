package org.example.app.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NettyServer {

    private static final int PORT = 8001;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private static final Logger LOGGER = Logger.getLogger(NettyServer.class.getName());

    public static void main(String[] args) {
        NettyServer server = new NettyServer();
        server.start();
    }

    public void start() {
        try {
            ServerBootstrap b = configure();

            ChannelFuture f = b.bind(PORT).sync();
            System.out.println("Server started and waiting for users...");

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, "Connection was interrupted");
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

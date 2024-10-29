package codefun.echoserver;

import codefun.echoserver.config.EchoServerConfig;
import codefun.echoserver.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;
import java.net.SocketAddress;


/**
 * start netty to serve
 */
@Slf4j
@SpringBootApplication
public class EchoServerApplication implements CommandLineRunner {
    @Autowired
    private EchoServerConfig echoServerConfig;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(EchoServerApplication.class, args);
    }

    private void startServer() throws Exception {
        log.info("start a nettyserver {} {}",
                echoServerConfig.getLocalAddr(), echoServerConfig.getLocalPort());

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            log.debug("init channel {}", ch);
                            ch.pipeline().addLast(null, null, new ServerHandler());
                        }
                    });

            ChannelFuture f = b.bind(new InetSocketAddress(
                    echoServerConfig.getLocalAddr(), echoServerConfig.getLocalPort()
            )).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        startServer();
    }
}
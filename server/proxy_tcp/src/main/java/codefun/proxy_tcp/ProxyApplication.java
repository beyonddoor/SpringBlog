package codefun.proxy_tcp;

import codefun.proxy_tcp.config.TcpProxyConfig;
import codefun.proxy_tcp.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * start netty to serve
 */
@Slf4j
@SpringBootApplication
public class ProxyApplication {
    @Autowired
    private TcpProxyConfig tcpProxyConfig;
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ProxyApplication.class, args);

        Connector.getInstance().init();
        new ProxyApplication().startServer();
        Connector.getInstance().shutdown();
    }

    void startServer() throws Exception{
        log.debug("start a nettyserver");

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
//                            ch.pipeline().addLast(new StringDecoder(), new StringEncoder(), new ServerHandler());
                            ch.pipeline().addLast(null, null, new ServerHandler());
                        }
                    });

            ChannelFuture f = b.bind(tcpProxyConfig.getLocalPort()).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
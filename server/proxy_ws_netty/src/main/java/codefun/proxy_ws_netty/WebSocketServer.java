package codefun.proxy_ws_netty;

import codefun.proxy_ws_netty.config.AppSetting;
import codefun.proxy_ws_netty.handler.CounterHandler;
import codefun.proxy_ws_netty.handler.FrontendHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class WebSocketServer {

    private final AppSetting appSetting;
    private final CounterHandler counterHandler;

    public WebSocketServer(AppSetting appSetting, CounterHandler counterHandler) {
        this.appSetting = appSetting;
        this.counterHandler = counterHandler;
    }

    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new ChunkedWriteHandler());
                            ch.pipeline().addLast(new HttpObjectAggregator(8192));
                            // here can add more endpoints
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler(appSetting.getWebSocketPath()));
                            ch.pipeline().addLast(counterHandler);
                            ch.pipeline().addLast(new FrontendHandler(appSetting));
                        }
                    });

            log.info("WebSocket server started at {} {}", appSetting.getLocalAddr(), appSetting.getLocalPort());
            bootstrap.bind(appSetting.getAddress())
                    .sync().channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

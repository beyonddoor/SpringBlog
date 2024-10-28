package codefun.load_test.logic.user;

import codefun.load_test.config.AppSetting;
import codefun.load_test.handler.TcpReadyHandler;
import codefun.load_test.handler.WebSocketReadyHandler;
import codefun.load_test.util.SpringContext;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Connector {
    private AppSetting appSetting;
    private User user;
    private static EventLoopGroup workerGroup = new NioEventLoopGroup();
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            workerGroup.shutdownGracefully();
        }));
    }

    public Connector(User user) {
        this.appSetting = SpringContext.getBean(AppSetting.class);
        this.user = user;
    }

    private void buildChannelPipeline(ChannelPipeline pipeline) {
        if (appSetting.getWebSocketPath() != null && !appSetting.getWebSocketPath().isEmpty()) {
            log.debug("add websocket handler");

            // Add SSL handler if necessary
//            if ("wss".equalsIgnoreCase(uri.getScheme())) {
//                SslContext sslContext = SslContextBuilder.forClient()
//                        .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
//                pipeline.addLast(sslContext.newHandler(ch.alloc(), uri.getHost(), uri.getPort()));
//            }

            var config = WebSocketClientProtocolConfig.newBuilder()
                    .webSocketUri(appSetting.getWebSocketPath())
                    .version(WebSocketVersion.V13)
                    .build();

            pipeline.addLast(new HttpClientCodec());
            pipeline.addLast(new HttpObjectAggregator(8192));
            pipeline.addLast(new ChunkedWriteHandler());
//            pipeline.addLast(WebSocketClientCompressionHandler.INSTANCE); // Add compression handler
            pipeline.addLast(new WebSocketClientProtocolHandler(config));
            pipeline.addLast(new WebSocketReadyHandler(user));
            return;
        }

        log.debug("add tcp handler");
        pipeline.addLast(new TcpReadyHandler(user));
    }

    public void stop() {
//        workerGroup.shutdownGracefully();
    }

    public ChannelFuture start() throws InterruptedException {
        log.debug("connector start");

        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        buildChannelPipeline(ch.pipeline());
                    }
                });

        log.debug("connect {} {}", appSetting.getHost(), appSetting.getPort());
        return b.connect(appSetting.getHost(), appSetting.getPort()).addListener(future -> {
            if (future.isSuccess()) {
                log.debug("connect success");
                user.onConnected();
            } else {
                log.debug("connect fail", future.cause());
                user.onConnectFailed();
            }
        });
    }
}

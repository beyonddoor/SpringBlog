package codefun.load_test.service;

import codefun.load_test.config.AppSetting;
import codefun.load_test.handler.ClientHandler;
import codefun.load_test.logic.UserManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Connector {
    private AppSetting appSetting;

    private UserManager userManager;

    public Connector(AppSetting appSetting, UserManager userManager) {
        this.appSetting = appSetting;
        this.userManager = userManager;
    }

    public void start() throws InterruptedException {
        log.debug("connector start");
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ClientHandler(appSetting, userManager));
                        }
                    });

            log.info("connect {} {}", appSetting.getHost(), appSetting.getPort());
            b.connect(appSetting.getHost(), appSetting.getPort()).addListener(future -> {
                if (future.isSuccess()) {
                    log.info("connect success");
                } else {
                    log.error("connect fail", future.cause());
                }
            });
//            b.connect(appSetting.getHost(), appSetting.getPort()).sync().channel().closeFuture().sync();
        } finally {
//            log.info("connect {} {}", appSetting.getHost(), appSetting.getPort());
//            workerGroup.shutdownGracefully();
        }
    }
}

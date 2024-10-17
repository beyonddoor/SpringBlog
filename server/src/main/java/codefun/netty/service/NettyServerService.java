package codefun.netty.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NettyServerService {

    private final ServerBootstrap serverBootstrap;

    public NettyServerService(ServerBootstrap serverBootstrap) {
        this.serverBootstrap = serverBootstrap;
    }

    @PostConstruct
    public void startServer() throws InterruptedException {
        log.debug("Starting server");
        ChannelFuture f = serverBootstrap.bind(8889).sync();
        f.channel().closeFuture().sync();
    }
}
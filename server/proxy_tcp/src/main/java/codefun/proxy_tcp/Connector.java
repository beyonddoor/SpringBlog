package codefun.proxy_tcp;


import codefun.proxy_tcp.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Connector {
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Bootstrap clientBootstrap = new Bootstrap();

    private static Connector instance = null;

    public static Connector getInstance() {
        if (instance == null) {
            synchronized (Connector.class) {
                if (instance == null) {
                    instance = new Connector();
                }
            }
        }
        return instance;
    }

    public synchronized void init() {
        clientBootstrap.group(workerGroup)
                .channel(NioSocketChannel.class);
//                .handler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    public void initChannel(SocketChannel ch) throws Exception {
//                        ch.pipeline().addLast(null, null, new ClientHandler());
//                    }
//                });
    }

    public synchronized ChannelFuture connect(String host, int port, Channel serverChannel) {
        try {
            clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ClientHandler(serverChannel));
                }
            });
            return clientBootstrap.connect(host, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}

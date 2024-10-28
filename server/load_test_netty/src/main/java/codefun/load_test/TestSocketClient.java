//package codefun.load_test;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.http.*;
//import io.netty.handler.codec.http.websocketx.*;
//import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
//
//import java.net.URI;
//public class TestSocketClient {
//
//    private final String url;
//
//    public TestSocketClient(String url) {
//        this.url = url;
//    }
//
//    public void start() throws Exception {
//        URI uri = new URI(url);
//        String host = uri.getHost();
//        int port = uri.getPort() == -1 ? (uri.getScheme().equals("wss") ? 443 : 80) : uri.getPort();
//
//        EventLoopGroup group = new NioEventLoopGroup();
//        try {
//            Bootstrap bootstrap = new Bootstrap();
//            bootstrap.group(group)
//                    .channel(NioSocketChannel.class)
//                    .handler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel ch) {
//                            ChannelPipeline pipeline = ch.pipeline();
//                            pipeline.addLast(new HttpClientCodec());
//                            pipeline.addLast(new HttpObjectAggregator(8192));
//                            pipeline.addLast(new WebSocketClientCompressionHandler());
//                            pipeline.addLast(new WebSocketClientProtocolHandler(WebSocketClientHandshakerFactory.newHandshaker(uri,
//                                    WebSocketVersion.V13, null, false, new DefaultHttpHeaders())));
//                            pipeline.addLast(new WebSocketClientHandler());
//                        }
//                    });
//
//            Channel channel = bootstrap.connect(host, port).sync().channel();
//
//            // Wait for the WebSocket handshake to complete
//            WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(uri,
//                    WebSocketVersion.V13, null, false, new DefaultHttpHeaders());
//            handshaker.handshake(channel).sync();
//
//            // Keep the channel open and do something
//            // E.g., sending a message to the server
//            channel.writeAndFlush(new TextWebSocketFrame("Hello from Netty WebSocket Client!"));
//
//            // Add an appropriate way to wait or loop
//            // ...
//
//        } finally {
//            // Clean up
//            group.shutdownGracefully();
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//        String url = "ws://localhost:8080/websocket"; // Replace with your WebSocket server URL
//        new TestSocketClient(url).start();
//    }
//}
//
//class WebSocketClientHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
//        if (frame instanceof TextWebSocketFrame) {
//            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
//            System.out.println("Received: " + textFrame.text());
//        } else {
//            // Handle other frame types: BinaryWebSocketFrame, PongWebSocketFrame, etc.
//            throw new UnsupportedOperationException("Unsupported frame type: " + frame.getClass().getName());
//        }
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();
//    }
//}
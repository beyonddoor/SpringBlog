package codefun.netty;

import codefun.ws_demo.util.ByteBufferUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private final WebSocketSession session;

    public ClientHandler(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Channel active");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Channel channelInactive");
        log.debug("close session");
        session.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("Channel channelRead");
        ByteBuf byteBuf = (ByteBuf) msg;
        session.sendMessage(new BinaryMessage(ByteBufferUtil.getAllReadableBytes(byteBuf)));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.debug("Channel channelReadComplete");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught local={} remote={}",
                ctx.channel().localAddress(),
                ctx.channel().remoteAddress(), cause);
        // 不同于disconnect的gracefully
        ctx.close();
    }
}
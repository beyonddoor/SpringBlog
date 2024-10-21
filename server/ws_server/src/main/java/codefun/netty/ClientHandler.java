package codefun.netty;

import codefun.ws_demo.util.ByteBufferUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * TODO 如何关联byte和binarymessage
 */
@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * accpet channel
     */
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
        // send all bytes in byteBuf to session

//        todo 可能的性能问题
        session.sendMessage(new BinaryMessage(ByteBufferUtil.getAllReadableBytes(byteBuf)));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.debug("Channel channelReadComplete");
    }
}

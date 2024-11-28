package codefun.load_test.handler;

import codefun.load_test.logic.user.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketReadyHandler extends ReadyHandlerBase {

    public WebSocketReadyHandler(User user) {
        super(user);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketClientProtocolHandler.ClientHandshakeStateEvent) {

            WebSocketClientProtocolHandler.ClientHandshakeStateEvent handshakeEvent =
                    (WebSocketClientProtocolHandler.ClientHandshakeStateEvent) evt;

            if (handshakeEvent == WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE) {
                log.debug("WebSocket handshake completed");
                user.getUserManager().onUserConnected(user);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
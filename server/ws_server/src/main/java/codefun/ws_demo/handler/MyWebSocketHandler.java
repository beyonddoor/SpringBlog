package codefun.ws_demo.handler;

import codefun.netty.Connector;
import codefun.ws_demo.config.AppSetting;
import codefun.ws_demo.util.ByteBufferUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Slf4j
@Component
public class MyWebSocketHandler extends AbstractWebSocketHandler {

    final AppSetting appSetting;

    public MyWebSocketHandler(AppSetting appSetting) {
        this.appSetting = appSetting;
    }

    /**
     * conn关闭，关闭channel
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        log.info("Connection closed: {}", session.getId());
        ChannelFuture channelFuture = (ChannelFuture) session.getAttributes().getOrDefault("channelFuture", null);
        if (channelFuture != null) {
            channelFuture.channel().close();
        }
    }

    private void sendBytesToBackendServer(byte[] payload, WebSocketSession session) {
//        TODO 关联一个channel
        if (!session.getAttributes().containsKey("channelFuture")) {
            ChannelFuture channelFuture = Connector.getInstance().connect(
                    appSetting.getBackendHost(), appSetting.getBackendPort(), session);
            session.getAttributes().put("channelFuture", channelFuture);
        }

        ChannelFuture channelFuture = (ChannelFuture) session.getAttributes().get("channelFuture");

        ByteBuf byteBuf = channelFuture.channel().alloc().buffer(payload.length);
        byteBuf.writeBytes(payload);
        channelFuture.channel().writeAndFlush(byteBuf);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.debug("Received text message from client {} ", message.getPayload());

        var payload = message.asBytes();
        sendBytesToBackendServer(payload, session);
    }

    /**
     * 有消息，创建channel
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        if(log.isDebugEnabled()) {
            log.debug("Received binary message from client {} ", ByteBufferUtil.toHexString(message.getPayload()));
        }

        // Get the binary data
        byte[] payload = message.getPayload().array();
        sendBytesToBackendServer(payload, session);


//        // Call backend server B with the binary data and get the response
////        byte[] response = sendToBackendServerB(payload);
//        byte[] response = "Hello from WebSocket Server A".getBytes();
//
//        // Send the response back to the client
//        session.sendMessage(new BinaryMessage(ByteBuffer.wrap(response)));
    }

//    private byte[] sendToBackendServerB(byte[] data) throws IOException {
//        // Replace with your backend server URL
//        String backendServerUrl = "http://localhost:8081/api";
//
//        HttpURLConnection connection = (HttpURLConnection) new URL(backendServerUrl).openConnection();
//        connection.setDoOutput(true);
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "application/octet-stream");
//
//        // Send the request data to Backend Server B
//        connection.getOutputStream().write(data);
//
//        // Read the response from Backend Server B
//        try (var in = connection.getInputStream()) {
//            return in.readAllBytes();
//        }
//    }
}
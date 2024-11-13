package codefun.util;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

public class ByteBufferUtil {
    public static String toHexString(ByteBuffer buffer) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buffer.remaining(); i++) {
            sb.append(String.format("%02X ", buffer.get(i)));
        }
        return sb.toString();
    }

    public static byte[] getAllReadableBytes(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }
}

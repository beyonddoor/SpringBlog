package codefun.load_test.logic;

import io.netty.buffer.ByteBuf;

/**
 * 消息编码器
 */
public interface IMessageEncoder {
    /**
     * 编码
     * @param buff
     * @param data
     * @return
     */
    Object encode(ByteBuf buff, byte[] data);
}

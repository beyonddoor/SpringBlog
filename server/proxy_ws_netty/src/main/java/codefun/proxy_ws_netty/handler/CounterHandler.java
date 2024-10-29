package codefun.proxy_ws_netty.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@ChannelHandler.Sharable
public class CounterHandler extends ChannelInboundHandlerAdapter {
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        counter.incrementAndGet();
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        counter.decrementAndGet();
        ctx.fireChannelInactive();
    }

    public int getCounter() {
        return counter.get();
    }
}

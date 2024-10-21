//package codefun.netty.service;
//
//import codefun.netty.Connector;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelFuture;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
///**
// * 连接gate的服务，适配connector
// */
//@Service
//public class ConnectorService {
//
//    public void init() {
//        Connector.getInstance().init();
//    }
//
//    public ChannelFuture connect(String host, int port, Channel serverChannel) {
//        return Connector.getInstance().connect(host, port, serverChannel);
//    }
//
//    public void shutdown() {
//        Connector.getInstance().shutdown();
//    }
//}

import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;


public class InetAddressTest {

    @Test
    public void test() {
        var addr = new InetSocketAddress("0.0.0.0", 8080);
        System.out.println(addr);
    }

    @Test
    public void test2() {
        String host = null;
        var addr = new InetSocketAddress(host, 8080);
        System.out.println(addr);
        addr = new InetSocketAddress(8080);
        System.out.println(addr);
    }
}

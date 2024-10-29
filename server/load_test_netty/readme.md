一个netty实现的性能测试客户端

注意使用netcat进行测试，有连接数的限制，似乎会阻塞在3个
socat - TCP-LISTEN:8081,fork

todo
1. ~~修复bytebuf的泄漏 DONE~~
2. ~~移除springboot server监听8080 DONE~~
3. 

package nio;
import	java.nio.channels.SelectionKey;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import	java.nio.channels.SocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/08/30
 */
public class NioServer {

    public static void main(String[] args) {
        try(ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.socket().bind(new InetSocketAddress(3388));

            Selector selector = Selector.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("服务器准备就绪，开始监听，端口3388");


            while (true) {
                int wait = selector.select();
                if (wait == 0)
                    continue;

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                while (iterator.hasNext()) {

                    SelectionKey key = iterator.next();

                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel channel = server.accept();
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                    } else if(key.isReadable()) {
                        SocketChannel server = (SocketChannel) key.channel();
                        int len = server.read(byteBuffer);
                        if (len > 0) {
                            byteBuffer.flip();
                            String content = new String(byteBuffer.array(), 0, len);
                            System.out.println(content);

                            server.configureBlocking(false);
                            server.register(selector, SelectionKey.OP_WRITE);
                        }
                        byteBuffer.clear();
                    } else if (key.isWritable()) {
                        SocketChannel server = (SocketChannel) key.channel();
                        server.write(ByteBuffer.wrap("Hello Client!".getBytes()));
                    }

                    iterator.remove();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

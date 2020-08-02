package nio;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import	java.nio.channels.SocketChannel;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/08/31
 */
public class NIOClient {

    public static void main(String[] args) {
        try(SocketChannel channel = SocketChannel.open()) {
            channel.connect(new InetSocketAddress(3388));

            //发送数据
            if (channel.isConnected()) {
                channel.write(ByteBuffer.wrap("Hello Server!".getBytes()));
            }

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int len = channel.read(buffer);
            String content = new String(buffer.array(), 0, len);
            System.out.println(content);
        }catch (Exception e) {

        }

    }
}

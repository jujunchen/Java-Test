package design.decorator;

/**
 * 通过层层构造，将装饰者组装到一起，增强被装饰者
 * @author jujun chen
 * @date 2020/07/26
 */
public class DecoratorTest {
    public static void main(String[] args) {
        IPacketCreator pc = new PacketHTTPHeaderCreator((new PacketHTMLHeaderCreator(new PacketBodyCreator())));
        System.out.println(pc.handleContent());
    }
}

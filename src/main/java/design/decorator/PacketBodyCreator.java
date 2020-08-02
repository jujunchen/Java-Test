package design.decorator;

/**
 * 处理HTTP主体内容
 * @author jujun chen
 * @date 2020/07/26
 */
public class PacketBodyCreator implements IPacketCreator {

    @Override
    public String handleContent() {
        //构造核心数据，但不包括格式
        return "Content of Packet";
    }
}

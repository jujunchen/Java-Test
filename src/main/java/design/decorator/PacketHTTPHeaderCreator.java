package design.decorator;

/**
 * 完成数据包HTTP头部处理
 * @author jujun chen
 * @date 2020/07/26
 */
public class PacketHTTPHeaderCreator extends PacketDecorator {

    public PacketHTTPHeaderCreator(IPacketCreator c) {
        super(c);
    }

    /**
     * 用于处理内容
     *
     * @return
     */
    @Override
    public String handleContent() {
        StringBuffer sb = new StringBuffer();
        sb.append("Cache-Control:no-cache\n");
        sb.append("Date:Mon,31Dec202004:25:58GMT\n");
        sb.append(component.handleContent());
        return sb.toString();
    }
}

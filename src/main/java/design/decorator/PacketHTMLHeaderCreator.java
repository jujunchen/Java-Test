package design.decorator;

/**
 * 处理HTML的具体装饰器
 * @author jujun chen
 * @date 2020/07/26
 */
public class PacketHTMLHeaderCreator extends PacketDecorator {

    public PacketHTMLHeaderCreator(IPacketCreator c) {
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
        sb.append("<html>");
        sb.append("<body>");
        sb.append(component.handleContent());
        sb.append("</body>");
        sb.append("</html>\n");
        return sb.toString();
    }
}

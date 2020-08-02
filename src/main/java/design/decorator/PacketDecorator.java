package design.decorator;

/**
 * 维护核心组件component,负责告诉子类，
 * 其核心业务逻辑应该全权委托component完成，自己仅做增强处理
 *
 * @author jujun chen
 * @date 2020/07/26
 */
public abstract class PacketDecorator implements IPacketCreator {
    IPacketCreator component;

    public PacketDecorator(IPacketCreator c) {
        component = c;
    }
}

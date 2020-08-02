package design.observer;

/**主题接口
 * @author jujun chen
 * @date 2020/07/26
 */
public interface ISubject {
    //添加观察者
    void attach(IObserver observer);

    //删除观察者
    void detach(IObserver observer);

    //通知所有观察者
    void inform();
}

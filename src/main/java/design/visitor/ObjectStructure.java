package design.visitor;

import java.util.LinkedList;
import java.util.List;

/**
 * 数据结构，可以管理很多人
 * @author jujun chen
 * @date 2020/07/20
 */
public class ObjectStructure {

    //维护了一个集合
    private List<Person> personList = new LinkedList<>();

    //增加到list
    public void attach(Person p) {
        personList.add(p);
    }

    //移除
    public void detach(Person p) {
        personList.remove(p);
    }

    //显示测评情况
    public void display(Action action) {
        for (Person p : personList) {
            p.accept(action);
        }
    }
}

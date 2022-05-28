package onJava.enums.multi;

/**
 * 分发类型接口
 */
public interface Item {
    Outcome compete(Item it);
    Outcome eval(Paper p);
    Outcome eval(Scissors s);
    Outcome eval(Rock r);
}

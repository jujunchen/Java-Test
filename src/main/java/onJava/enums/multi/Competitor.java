package onJava.enums.multi;

/**
 * 比较器
 * @param <T>
 */
public interface Competitor<T extends Competitor<T>> {

    Outcome compete(T competitor);
}

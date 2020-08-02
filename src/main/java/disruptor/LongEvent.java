package disruptor;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/07/24
 */
public class LongEvent {
    private long value;

    public void set(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LongEvent{" +
                "value=" + value +
                '}';
    }
}

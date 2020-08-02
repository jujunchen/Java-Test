import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author jujun chen
 * @date 2020/06/06
 */
public class LongAccumulatorTest {

    @Test
    public void LongAccumulatorTest() {
        LongAccumulator longAccumulator = new LongAccumulator((x,y) -> x * y, 2);
        //2 * 10
        longAccumulator.accumulate(10);
        assert  longAccumulator.get() == 20;
        //重置为2
        longAccumulator.reset();
        assert longAccumulator.get() == 2;

        LongAccumulator longAccumulator1 = new LongAccumulator((x,y) -> x + y, 0);
        longAccumulator1.accumulate(1);
        System.out.println(longAccumulator1.get());

        LongAdder longAdder = new LongAdder();
        longAdder.add(1);
        System.out.println(longAdder.sum());
    }


    @Test
    public void tes1() {
        int a = 1;
        int b = 2;
        int c = 3;
        if (a > 0 || b > 2 && c > 2) {
            System.out.println("123");
        }
    }
}

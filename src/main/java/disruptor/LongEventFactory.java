package disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/07/24
 */
public class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}

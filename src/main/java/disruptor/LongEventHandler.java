package disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/07/24
 */
public class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
        System.out.println("longEvent:" + longEvent);
    }
}


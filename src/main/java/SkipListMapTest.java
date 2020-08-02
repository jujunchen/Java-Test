import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/07/24
 */
public class SkipListMapTest {
    public static void main(String[] args) {
        ConcurrentSkipListMap skipListMap = new ConcurrentSkipListMap();
        skipListMap.put("a", "a1");
        skipListMap.put("b", "b1");
        skipListMap.descendingMap();
        System.out.println(skipListMap);

        System.out.println(SingleObj.getInstance());
        System.out.println(SingleObj.getInstance());
    }
}

class SingleObj {

    public static Event getInstance() {
        return EventObj.event;
    }
    
    static class EventObj {
        private static final Event event = new Event();
    }
}

class Event {
    private String name;
}

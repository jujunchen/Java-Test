/**
 * @author: jujun chen
 * @Type
 * @description: volatile 描述对象，多线程测试，循环中不加耗时操作，不可见，加耗时操作可见
 * @date: 2019/08/24
 */
public class VolatileTestAgain implements Runnable {
    private volatile ObjectA a; // 加上volatile也无法结束While循环了
    public VolatileTestAgain(ObjectA a) {
        this.a = a;
    }

    public ObjectA getA() {
        return a;
    }

    public void setA(ObjectA a) {
        this.a = a;
    }

    @Override
    public void run() {
        long i = 0;
        ObjectASub sub = a.getSub();
        while (!sub.isFlag()) {
            i++;
            System.out.println(i);
        }
        System.out.println("stop My Thread " + i);
    }

    public static void main(String[] args) throws InterruptedException {
        // 如果启动的时候加上-server 参数则会 输出 Java HotSpot(TM) Server VM
        System.out.println(System.getProperty("java.vm.name"));
        ObjectASub sub = new ObjectASub();
        ObjectA sa = new ObjectA();
        sa.setSub(sub);
        VolatileTestAgain test = new VolatileTestAgain(sa);
        new Thread(test).start();

        Thread.sleep(1000);
        sub.setFlag(true);
        Thread.sleep(1000);
        System.out.println("Main Thread " + sub.isFlag());
    }

    static class ObjectA {
        private ObjectASub sub;

        public ObjectASub getSub() {
            return sub;
        }

        public void setSub(ObjectASub sub) {
            this.sub = sub;
        }
    }

    static class ObjectASub{
        private boolean flag;

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }


    }
}

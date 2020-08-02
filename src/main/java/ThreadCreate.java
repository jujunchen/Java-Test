import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * @author: jujun chen
 * @Type ThreadCreate
 * @description:
 * @date: 2019/06/11
 */
public class ThreadCreate {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread1();
        thread.start();
//        TimeUnit.SECONDS.sleep(3);
//        thread.stop();
        thread.interrupt();
    }
}

class Thread1 extends Thread{

    @Override
    public void run() {
        while (true) {
            System.out.println("Thread1 run...");
            try {
                TimeUnit.SECONDS.sleep(1);
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

class Thread2 implements Runnable{
    @Override
    public void run() {
        System.out.println("Thread2 run...");
    }
}




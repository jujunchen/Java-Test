/**
 * @author: jujun chen
 * @description:
 * @date: 2019-04-22
 */
public class LamRunnable {

    public static void main(String[] args) {
        Runnable r = () -> System.out.println("hello lam");

        r.run();

    }

}

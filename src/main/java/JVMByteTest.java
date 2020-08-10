import java.util.concurrent.TimeUnit;

/**
 * 字空间测试
 * @author: jujun chen
 * @description:
 * @date: 2020-08-08
 */
public class JVMByteTest {

    public static void main(String[] args) throws InterruptedException {
        test4();
        TimeUnit.SECONDS.sleep(30);
    }

    public static void test1(long a, long b, long c) {
        long d, e, f;
    }

    //会回收b的字空间
    public static void test2() {
        {
            byte[] b = new byte[6*1024*1024];
        }
        int a = 0;
        System.gc();
        System.out.println("first explict gc over");
    }

    //不会回收b的字空间
    public static void test3() {
        {
            int c = 0;
            byte[] b = new byte[6*1024*1024];
        }
        //复用c的空间
        int a = 0;
        System.gc();
        System.out.println("first explict gc over");
    }

    public static void test4() {
        {
            int c = 0;
            byte[] b = new byte[6*1024*1024];
        }
        //复用c的空间
        int a = 0;
        //复用b的空间
        int d = 0;
        System.gc();
        System.out.println("first explict gc over");
    }


}

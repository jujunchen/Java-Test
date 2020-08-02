/**
 * @author: jujun chen
 * @Type
 * @description:  https://mp.weixin.qq.com/s/jsZv2ZJKbOEtPjlLNgs0bQ
 *  主要是静态方法的时候碰到了静态实例变量的加载，实例又是自己的实例
 * @date: 2019/08/13
 */
public class JvmStaticTest {

    public static void main(String[] args) {
        staticFunction();
    }

    static JvmStaticTest test = new JvmStaticTest();

    static {
        System.out.println("1");
    }

    {
        System.out.println(2);
    }

    JvmStaticTest() {
        System.out.println(3);
        System.out.println("a="+ a + ",b="+b);
    }

    private static void staticFunction() {
        System.out.println(4);
    }

    int a = 110;
    static int b = 112;


}

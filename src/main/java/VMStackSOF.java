
/**
 * @author: jujun chen
 * @description: -Xss160k
 * @date: 2019/4/7
 */
public class VMStackSOF {
    private int stackLength = 1;
    public void stackLeak(){
        stackLength++;
        stackLeak();
    }

    /*public static void main(String[] args) {
        VMStackSOF oom = new VMStackSOF();
        oom.stackLeak();
    }*/

    static int count = 0;
    public static void main(String[] args) {
        try {
            stackMethod();
        } catch(Error err) {
            err.printStackTrace();
            System.out.println("执行count=" + count);
        }
    }
    private static void stackMethod() {
        count ++;
        stackMethod();
    }

}

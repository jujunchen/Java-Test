/**
 * @author: jujun chen
 * @description:
 * @date: 2019-04-22
 */
public class MyFuncationTest {

    public static void main(String[] args) {
        MyFuncation funcation = x -> x * x;
        System.out.println(funcation.getValue(100));
    }
}

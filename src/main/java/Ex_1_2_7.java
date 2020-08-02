/**
 * @author: jujun chen
 * @description:
 * @date: 2019/3/28
 */
public class Ex_1_2_7 {
    public static void main(String[] args) {
        String s = "abcdefg";
        System.out.println(mystery(s));
    }

    private static String mystery(String s){
        int n = s.length();
        if (n <= 1) return s;
        String a = s.substring(0,n/2);
        String b = s.substring(n/2,n);
        return mystery(b) + mystery(a);
    }
}

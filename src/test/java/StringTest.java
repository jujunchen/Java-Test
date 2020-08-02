import org.junit.Test;

import java.util.StringTokenizer;

/**
 * @author jujun chen
 * @date 2020/08/02
 */
public class StringTest {

    @Test
    public void test1() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 1000000; i++) {
            stringBuilder.append(i);
            stringBuilder.append(";");
        }
        String newStr = stringBuilder.toString();

        //性能第二
        StringTokenizer stringTokenizer = new StringTokenizer(newStr, ";");
        long start = System.currentTimeMillis();
        while (stringTokenizer.hasMoreTokens()) {
            stringTokenizer.nextToken();
        }
        long end = System.currentTimeMillis();
        System.out.println("stringTokenizer:" + (end - start));

        //性能最高
        start = System.currentTimeMillis();
        newStr.split(";");
        end = System.currentTimeMillis();
        System.out.println("split:" + (end - start));


        //无法结束
        String tmp = newStr;
        start = System.currentTimeMillis();
        while (true) {
            int j = tmp.indexOf(";");
            if (j < 0) break;
            tmp.substring(0, j);
            tmp = tmp.substring(j + 1);
        }
        end = System.currentTimeMillis();
        System.out.println("me:" + (end -start));
    }

    /**
     +: 146
     concat: 32
     stringBuilder: 2
     stringBuffer: 1
     */
    @Test
    public void test2() {
        long start = System.currentTimeMillis();
        String str = "";
        for (int i = 0; i < 10000; i++) {
            str = str + i;
        }
        long end = System.currentTimeMillis();
        System.out.println("+: " + (end - start));


        start = System.currentTimeMillis();
        str = "";
        for (int i = 0; i < 10000; i++) {
            str  = str.concat(String.valueOf(i));
        }
        end = System.currentTimeMillis();
        System.out.println("concat: " + (end - start));

        start = System.currentTimeMillis();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            stringBuilder.append(i);
        }
        end = System.currentTimeMillis();
        System.out.println("stringBuilder: " + (end - start));


        start = System.currentTimeMillis();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 10000; i++) {
            stringBuffer.append(i);
        }
        end = System.currentTimeMillis();
        System.out.println("stringBuffer: " + (end - start));
    }
}

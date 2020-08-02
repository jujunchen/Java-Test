package synthetictest;

import java.util.Calendar;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/08/29
 */
public final class DemonstrateSyntheticMethods
{
    public static void main(final String[] arguments)
    {
        DemonstrateSyntheticMethods.NestedClass nested =
                new DemonstrateSyntheticMethods.NestedClass();
        System.out.println("String: " + nested.highlyConfidential);
    }

    private static final class NestedClass
    {
        private String highlyConfidential = "Don't tell anyone about me";
    }
}

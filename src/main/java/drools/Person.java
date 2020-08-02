package drools;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/07/21
 */
@Data
@Accessors(chain = true)
public class Person {
    private String name;
    private int age;
    private String className;
}

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
public class School {
    private String className;
    private int classCount;

    private String[] classNameAry;
}



package drools;

import lombok.Data;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/07/15
 */
@Data
public class Product {
    public static final String DIAMOND = "DIAMOND";

    public static final String GOLD = "GOLD";

    private String type;

    private int discount;
}

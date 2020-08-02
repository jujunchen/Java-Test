package spring;

import org.springframework.stereotype.Service;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/09/07
 */
@Service
public class SmartSeller implements Seller {

    @Override
    public void sell(String goods) {
        System.out.println("sell " + goods);
    }
}

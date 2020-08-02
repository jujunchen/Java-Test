package spi;

import java.rmi.Remote;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/09/15
 */
public class TextRemove implements RemoveInterface {
    @Override
    public void remove() {
        System.out.println("文字删除成功");
    }
}

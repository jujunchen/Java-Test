package spi;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/09/15
 */
public class FileRemove implements RemoveInterface {
    @Override
    public void remove() {
        System.out.println("文件删除成功");
    }
}

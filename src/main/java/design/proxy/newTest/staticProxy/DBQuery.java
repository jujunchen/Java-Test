package design.proxy.newTest.staticProxy;

import java.util.concurrent.TimeUnit;

/**
 * 重量级对象，假设构建会比较慢
 * @author jujun chen
 * @date 2020/07/25
 */
public class DBQuery implements IDBQuery {

    public DBQuery() {
        try {
            //假设包含耗时操作
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String request() {
        return "request string";
    }
}

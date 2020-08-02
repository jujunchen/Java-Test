package design.proxy.newTest.staticProxy;

/**
 * 轻量级对象
 * @author jujun chen
 * @date 2020/07/25
 */
public class DBQueryProxy implements IDBQuery{

    private DBQuery real;

    @Override
    public String request() {
        if (real == null)
            real = new DBQuery();

        return real.request();
    }
}

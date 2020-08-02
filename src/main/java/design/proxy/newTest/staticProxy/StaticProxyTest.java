package design.proxy.newTest.staticProxy;

/**
 * @author jujun chen
 * @date 2020/07/25
 */
public class StaticProxyTest {

    public static void main(String[] args) {
        IDBQuery query = new DBQueryProxy();
        query.request();
    }
}

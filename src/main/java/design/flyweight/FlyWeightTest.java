package design.flyweight;

/**
 * 具体实现类
 * @author jujun chen
 * @date 2020/07/25
 */
public class FlyWeightTest {

    public static void main(String[] args) {
        ReportManagerFactory rmf = new ReportManagerFactory();
        IReportManager rm = rmf.getFinancialReportManager("A");
        System.out.println(rm.createReport());
    }
}

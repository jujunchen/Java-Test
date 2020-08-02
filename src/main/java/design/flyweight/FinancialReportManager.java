package design.flyweight;

/**
 * 具体享元类，可以继承抽象享元，也可以直接实现，财务报表
 * @author jujun chen
 * @date 2020/07/25
 */
public class FinancialReportManager implements IReportManager {

    protected String tenantId = null;

    public FinancialReportManager(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String createReport() {
        return "This is a financial report";
    }
}

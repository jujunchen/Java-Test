package design.flyweight;

/**
 * 具体享元类，可以继承抽象享元，也可以直接实现，员工报表
 * @author jujun chen
 * @date 2020/07/25
 */
public class EmployeeReportManager implements IReportManager {
    protected String tenantId = null;

    public EmployeeReportManager(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String createReport() {
        return "This is a employee report";
    }
}

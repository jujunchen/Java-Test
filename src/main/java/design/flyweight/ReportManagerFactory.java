package design.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * 享元工厂类，报表工厂类
 * @author jujun chen
 * @date 2020/07/25
 */
public class ReportManagerFactory {

    Map<String, IReportManager> financialReportManager = new HashMap<>();
    Map<String, IReportManager> employeeReportManager = new HashMap<>();

    //获取财务报表管理类
    public IReportManager getFinancialReportManager(String tenantId) {
        IReportManager r = financialReportManager.get(tenantId);
        if (r == null) {
            r = new FinancialReportManager(tenantId);
            financialReportManager.put(tenantId, r);
        }
        return r;
    }

    //获取员工报表管理类
    public IReportManager getEmployeeReportReportManager(String tenantId) {
        IReportManager r = employeeReportManager.get(tenantId);
        if (r == null) {
            r = new EmployeeReportManager(tenantId);
            employeeReportManager.put(tenantId, r);
        }
        return r;
    }
}

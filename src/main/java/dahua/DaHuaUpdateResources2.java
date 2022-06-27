package dahua;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.poi.excel.ExcelReader;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * JSON 转 Excel
 * @author chenjujun
 * @date 12/8/20
 */
public class DaHuaUpdateResources2 {

    public static void main(String[] args) throws InterruptedException {
        //excel读取组织数据
        ExcelReader excelReader = new ExcelReader("/Users/chenjujun/Downloads/立林设备导入.xlsx", 0);
        ExcelReader excelReader1 = new ExcelReader("/Users/chenjujun/Downloads/立林设备导入.xlsx", 1);
        List<Map<String, Object>> excelData = excelReader.readAll();
        List<Map<String, Object>> excelData1 = excelReader1.readAll();
        System.out.printf("一共%s条数据\n", excelData.size());

        for (Map<String, Object> excelDatum : excelData) {
            String resource_name = (String) excelDatum.get("resource_name");
            String id = "";
            for (Map<String, Object> excelDatum1 : excelData1) {
                String resource_name1 = (String) excelDatum1.get("name");
                
                if (StrUtil.equals(resource_name, resource_name1)) {
                    id = (String) excelDatum1.get("id");
                    break;
                }
            }
            if (StrUtil.isNotBlank(id)) {
                System.out.println(id);
                id = "";
            } else {
                System.out.println("-");
            }
        }


    }

    @Data
    static class ResourceBean {
        private Long id;
        private Integer status;
        private Long resourceRegion;
        private String deviceImei;
        private Integer deviceStatus;
        private String deviceModel;
        private String resourceCode;
    }

}



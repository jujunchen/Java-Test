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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JSON 转 Excel
 * @author chenjujun
 * @date 12/8/20
 */
public class DaHuaUpdateResources {

    public static void main(String[] args) {
        //excel读取组织数据
        ExcelReader excelReader = new ExcelReader("/Users/chenjujun/Desktop/要删除的id.xlsx", 0);
        List<Map<String, Object>> excelData = excelReader.readAll();

        for (Map<String, Object> excelDatum : excelData) {
            ResourceBean resourceBean = new ResourceBean();
            resourceBean.setId(Long.parseLong(excelDatum.get("id").toString()));
//            resourceBean.setResourceRegion(Long.parseLong(excelDatum.get("space_id").toString()));
            resourceBean.setStatus(1);
            String body = JSON.toJSONString(resourceBean);
            HttpRequest httpRequest = HttpRequest.post("https://greenshop.gtdreamlife.com/config-tool/api/resourceMock");
            httpRequest.header("Authorization", "");
            ResourceBean retResources = JSON.parseObject(httpRequest.body(body).execute().body(), ResourceBean.class);
            System.out.println(retResources.getId() + "/" + retResources.getResourceRegion());
        }


    }

    @Data
    static class ResourceBean {
        private Long id;
        private Integer status;
        private Long resourceRegion;
    }

}



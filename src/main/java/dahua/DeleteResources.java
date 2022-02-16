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
 * 批量删除
 * @author chenjujun
 * @date 12/8/20
 */
public class DeleteResources {

    public static void main(String[] args) throws InterruptedException {
        //excel读取组织数据
        ExcelReader excelReader = new ExcelReader("/Users/chenjujun/Desktop/resources_list_打点.xlsx", 0);
        List<Map<String, Object>> excelData = excelReader.readAll();
        System.out.printf("一共%s条数据\n", excelData.size());

        // String questionJson = FileUtil.readUtf8String("/Users/chenjujun/绿城资料/导入/问题resourcesList.json");
        // JSONArray questionJsonArray = JSON.parseArray(questionJson);

       /*  String spaceJson = FileUtil.readUtf8String("/Users/chenjujun/绿城资料/导入/海月mapperspace.json");
        JSONArray spaceJsonArray = JSON.parseArray(spaceJson);
        Map<String, Long> spaceMap = new HashMap<>();
        for (Object object : spaceJsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            spaceMap.put(jsonObject.getString("subsystem_space_id"), jsonObject.getLong("gt_space_id"));
        } */

        for (Map<String, Object> excelDatum : excelData) {
        // for (Object object : questionJsonArray) {
            // JSONObject jsonObject = (JSONObject) object;
            
            // ResourceBean resourceBean = new ResourceBean();
            // resourceBean.setId(Long.parseLong(excelDatum.get("id").toString()));
            // resourceBean.setId(jsonObject.getLong("id"));
//            resourceBean.setResourceRegion(Long.parseLong(excelDatum.get("space_id").toString()));
            // resourceBean.setResourceRegion(spaceMap.get(jsonObject.getString("subsystem_region")));
            // resourceBean.setDeviceImei("");
            // resourceBean.setDeviceStatus(-1);
            /* if (resourceBean.getResourceRegion() == null) {
                System.out.println(resourceBean.getId() + "-获取不到空间");
                continue;
            } */
            // resourceBean.setStatus(1);
            // String body = JSON.toJSONString(resourceBean);
            String id = excelDatum.get("id").toString();
            HttpRequest httpRequest = HttpRequest.delete("https://operationgw.gtdreamlife.com/config/resourceslist/" + id);
            httpRequest.header("access-token", "66d94a0abd3140ebb7a8a434841fbe92");
            String result = httpRequest.execute().body();
            System.out.println(result);
            // ResourceBean retResources = JSON.parseObject(result, ResourceBean.class);
            // System.out.println(retResources.getId() + "/" + retResources.getResourceRegion());
            TimeUnit.MILLISECONDS.sleep(500);
        }


    }

    @Data
    static class ResourceBean {
        private Long id;
        private Integer status;
        private Long resourceRegion;
        private String deviceImei;
        private Integer deviceStatus;
    }

}



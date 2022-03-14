package dahua;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenjujun
 * @date 2021/10/9
 */
public class DeviceJSONTExcel {

    public static void main(String[] args) {
        String json = FileUtil.readUtf8String("/Users/chenjujun/java-projects/Java-Test/src/main/java/dahua/dir/斗潭设备.json");
        JSONArray jsonArray = JSON.parseObject(json).getJSONArray("data");

        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> firstMap = new HashMap<>();
        firstMap.put("indexCode", "");
        firstMap.put("name", "");
        String cm = "斗潭设备";
        String sheet = cm;
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            String indexCode = jsonObject.getString("indexCode");
            String name = jsonObject.getString("name");
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("indexCode", indexCode);
            dataMap.put("name", name);
            dataList.add(dataMap);
        }
        ExcelWriter excelWriter = new ExcelWriter("/Users/chenjujun/java-projects/Java-Test/src/main/java/dahua/dir/斗潭设备对比.xlsx",sheet);
        excelWriter.write(dataList);
        excelWriter.flush();
    }
}

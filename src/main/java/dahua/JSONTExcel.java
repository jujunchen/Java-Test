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
public class JSONTExcel {

    public static void main(String[] args) {
        String json = FileUtil.readUtf8String("/Users/chenjujun/Desktop/银杏园设备.json");
        JSONArray jsonArray = JSON.parseObject(json).getJSONArray("data");

        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> firstMap = new HashMap<>();
        firstMap.put("设备编码", "");
        firstMap.put("设备名称", "");
        firstMap.put("所属园区", "");
        String cm = "银杏园";
        String sheet = cm;
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            String code = jsonObject.getString("code");
            String name = jsonObject.getString("name");
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("设备编码", code);
            dataMap.put("设备名称", name);
            dataMap.put("所属园区", cm);
            dataList.add(dataMap);
        }
        ExcelWriter excelWriter = new ExcelWriter("/Users/chenjujun/Desktop/如心.xlsx",sheet);
        excelWriter.write(dataList);
        excelWriter.flush();
    }
}

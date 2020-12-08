import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelReader;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
public class DaHuaDeptIdTExcel {

    public static void main(String[] args) {
        String sqlTmp = "insert into `config_tool`.`ct_mapper_user_org` ( `connect_system_id`, `type`, `gt_space_id`, `subsystem_org_id`, `gmt_create`, `gmt_modify`, `status`, `space_name`, `type_name`) values ( '96', '%s', '%s', '%s', '%s', '%s', '0', '%s', '%s');";
        String json = FileUtil.readUtf8String("/Users/chenjujun/Desktop/deptjson.txt");
        JSONArray jsonArray = JSON.parseObject(json).getJSONArray("data");
        //转换后的大华弱电系统部门数据
        List<Map<String, String>> jsonData = new ArrayList<>();
        List<Object> jsonList = jsonArray.parallelStream().filter(item -> !((JSONObject)item).getString("id").equals("1")).sorted(Comparator.comparing(item -> ((JSONObject) item).getLong("id"))).collect(Collectors.toList());
        for (Object o : jsonList) {
            JSONObject jsonObject = (JSONObject) o;
            String orgCode  = jsonObject.getString("id");
            String orgName = jsonObject.getString("name");
            Map<String, String> map = new HashMap<>();
            map.put("orgCode", orgCode);
            map.put("orgName", orgName);
            buildJsonData(jsonList, jsonObject,  map);
            //移除项目所在组织，不生成sql
            if (!orgCode.equals("2")) {
                jsonData.add(map);
            }
        }
        //excel读取组织数据
        ExcelReader excelReader = new ExcelReader("/Users/chenjujun/Desktop/海月花园2.xlsx", 0);
        List<Map<String, Object>> excelData = excelReader.readAll();
        StringBuilder stringBuilder = new StringBuilder();
        for (Map<String, String> jsonMap : jsonData) {
            String orgName = jsonMap.get("orgName").substring(5);
            String sql = matchDataTSql(orgName, excelData, jsonMap, sqlTmp);
            if (sql == null) {
                System.out.println(jsonMap.get("orgCode"));
            } else {
                stringBuilder.append(sql);
                stringBuilder.append("\r\n");
            }
        }

        FileUtil.writeUtf8String(stringBuilder.toString(), "/Users/chenjujun/Desktop/mapper_user_org.sql");
    }

    private static String matchDataTSql(String orgName, List<Map<String, Object>> excelData, Map<String, String> jsonMap, String sqlTmp) {
        for (Map<String, Object> data : excelData) {
            String spaceName = String.valueOf(data.get("community_tree_path"));
            //根据路径全称匹配
            if (orgName.equals(spaceName.trim())) {
                //格式化sql
                String sql = String.format(sqlTmp, Space.getType(String.valueOf(data.get("type_code"))), data.get("space_id"), jsonMap.get("orgCode"), DateUtil.now(), DateUtil.now(), jsonMap.get("orgName"), data.get("type_code"));
                return sql;
            }
        }
        return null;
    }


    private static void buildJsonData(List<Object> jsonArray, JSONObject next, Map<String, String> map) {
        String orgCode = next.getString("id");
        String parentCode = next.getString("parentId");
        for (Object o : jsonArray) {
            JSONObject jsonObject1 = (JSONObject) o;
            String orgCode1 = jsonObject1.getString("id");
            String orgName1 = jsonObject1.getString("name");
            if (orgCode1.equals(parentCode)) {
                map.put("orgName", orgName1 + "-" +map.get("orgName"));
                buildJsonData(jsonArray, jsonObject1, map);
            }
        }
    }

    enum Space {
        PROJECT(1),
        COMMUNITY(2),
        PUBLIC_AREA(6),
        BUILDING(4),
        UNIT(3),
        HOUSE(5)
        ;

        private Integer type;

        Space(Integer type) {
            this.type = type;
        }

        public static Integer getType(String typeCode) {
            for (Space value : values()) {
                if (value.toString().equalsIgnoreCase(typeCode)) {
                    return value.type;
                }
            }
            return null;
        }
    }

}



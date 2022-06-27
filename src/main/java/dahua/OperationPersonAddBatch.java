package dahua;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.poi.excel.ExcelReader;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author chenjujun
 * @date 4/21/21
 */
public class OperationPersonAddBatch {

    public static void main(String[] args) {
        ExcelReader excelReader = new ExcelReader("/Users/chenjujun/绿城资料/导入/海月花园.xlsx", 0);
        List<Map<String, Object>> excelData = excelReader.readAll();

        ExcelReader spaceExcelReader = new ExcelReader("/Users/chenjujun/绿城资料/导入/海月花园空间.xlsx", 0);
        List<Map<String, Object>> spaceExcelData = spaceExcelReader.readAll();
        Map<String, String> spaceMap = new HashMap<>();
        spaceExcelData.forEach(item -> {
            String spaceId = String.valueOf(item.get("space_id"));
            String communityTreePath = (String) item.get("community_tree_path");
            spaceMap.put(communityTreePath, spaceId);
        });

//        ExcelReader cqrExcelReader = new ExcelReader("/Users/chenjujun/Desktop/衢州礼贤淳礼园人员导入.xls", 1);
//        List<Map<String, Object>> cqrExcelData = cqrExcelReader.readAll();
//        Map<String, String> cqrMap = new HashMap<>();
        /*cqrExcelData.forEach(item -> {
            String cqr = String.valueOf(item.get("姓名"));
            String communityTreePath = (String) item.get("房屋");
            cqrMap.put(communityTreePath + "/" + cqr, cqr);
        });*/

        String url = "https://operationgw.gtdreamlife.com/householder";

        int row = 0;

        //错误记录
        List<Person> errorList = new ArrayList<>();

        for (Map<String, Object> item : excelData) {
            try {
                String dept = (String) item.get("*现居地址");
                dept = dept.replace("根节点-南星街道-", "");
                String houseId = spaceMap.get(dept);
                String idCard = String.valueOf(item.get("身份证号"));
                String name = String.valueOf(item.get("*姓名"));
                String phone = String.valueOf(item.get("手机号码"));
                String customerType = String.valueOf(item.get("住户类型"));
                if (StringUtils.isBlank(phone)) {
                    System.out.println("没有手机号，跳过:" + row);
                    continue;
                }
                /*Person person = null;
                if (cqrMap.get(dept + "/" + name) != null) {
                    person = Person.builder().customerType("OWNER").houseId(houseId).idcard(idCard).name(name).userPhone(phone).build();
                    if (StringUtils.isBlank(idCard)) {
                        System.out.println("业主没有省份证号，跳过:" + row);
                        continue;
                    }
                } else {
                    person = Person.builder().customerType("KINSFOLK").houseId(houseId).idcard(idCard).name(name).userPhone(phone).build();
                }*/
                Person person = Person.builder().customerType("OWNER").houseId(houseId).idcard(idCard).name(name).userPhone(phone).build();
                JSONObject resultObject = sendData(url, JSON.toJSONString(person));
                if (!resultObject.getString("code").equals("0")) {
                    String msg = resultObject.getString("msg");
                    System.out.println("新增失败：" + person + " " + msg);
                    if ("该房屋已存在一个业主".equals(msg) || StringUtils.isBlank(idCard)) {
                        person = Person.builder().customerType("KINSFOLK").houseId(houseId).idcard(idCard).name(name).userPhone(phone).build();
                        resultObject = sendData(url, JSON.toJSONString(person));
                        if (!resultObject.getString("code").equals("0")) {
                            msg = resultObject.getString("msg");
                            System.out.println("新增失败：" + person + " " + msg);

                            person.setErrMsg(msg);
                            errorList.add(person);
                        } else {
                            System.out.println("新增成功:" + person);
                        }
                    } else {
                        //失败
                        person.setErrMsg(msg);
                        errorList.add(person);
                    }
                } else {
                    System.out.println("新增成功:" + person);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            ThreadUtil.sleep(300, TimeUnit.MILLISECONDS);
            row++;
        }
        //打印错误数据
        for (Person person : errorList) {
            System.out.println(person.getName() + " " + person.getUserPhone() + " " + person.getIdcard() + " " + person.getErrMsg());
        }
    }


    private static JSONObject sendData(String url, String body) {
        HttpRequest httpRequest = HttpRequest.post(url);
        httpRequest.header("access-token", "8a58263c9be84ee090c7ed9f9e6ce59c");
        String result = httpRequest.body(body).execute().body();
        return JSON.parseObject(result);
    }
}

@Data
@Builder
class Person {

    /**
     * 搬离时间
     */
    private String checkOutTime;

    /**
     * 省份类型
     */
    private String customerType;

    /**
     * 房屋id
     */
    private String houseId;

    /**
     * 身份证
     */
    private String idcard;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String userPhone;

    private String errMsg;
}
